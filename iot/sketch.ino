#include <WiFi.h>
#include <PubSubClient.h>

const char* WIFI_SSID = "Wokwi-GUEST";
const char* WIFI_SENHA = "";

const char* MQTT_BROKER = "broker.hivemq.com";
const int MQTT_PORTA = 1883;
const char* MQTT_TOPICO = "fatec-dsm/smartgym/sensores/presenca";

const char* CODIGO_SENSOR = "SENSOR-001";

const int PINO_PIR = 13;
const int PINO_LED = 2;
const unsigned long INTERVALO_ENVIO_MS = 2000;

WiFiClient wifiClient;
PubSubClient mqttClient(wifiClient);

bool ultimoEstado = false;
unsigned long ultimoEnvio = 0;

void conectarWiFi() {
  Serial.print("Conectando ao Wi-Fi ");
  Serial.print(WIFI_SSID);
  WiFi.mode(WIFI_STA);
  WiFi.begin(WIFI_SSID, WIFI_SENHA);
  while (WiFi.status() != WL_CONNECTED) {
    delay(250);
    Serial.print(".");
  }
  Serial.println();
  Serial.print("Wi-Fi conectado. IP: ");
  Serial.println(WiFi.localIP());
}

void conectarMqtt() {
  while (!mqttClient.connected()) {
    String clientId = "smartgym-esp32-" + String((uint32_t)esp_random(), HEX);
    Serial.print("Conectando ao broker MQTT ");
    Serial.print(MQTT_BROKER);
    Serial.print(" ... ");
    if (mqttClient.connect(clientId.c_str())) {
      Serial.println("conectado");
    } else {
      Serial.print("falhou, rc=");
      Serial.print(mqttClient.state());
      Serial.println(" tentando novamente em 3s");
      delay(3000);
    }
  }
}

void publicarLeitura(bool emUso) {
  char mensagem[96];
  snprintf(mensagem, sizeof(mensagem), "{\"codigoSensor\":\"%s\",\"emUso\":%s}", CODIGO_SENSOR, emUso ? "true" : "false");
  bool enviado = mqttClient.publish(MQTT_TOPICO, mensagem);
  Serial.print(enviado ? "Publicado em " : "Falha ao publicar em ");
  Serial.print(MQTT_TOPICO);
  Serial.print(": ");
  Serial.println(mensagem);
}

void setup() {
  Serial.begin(115200);
  pinMode(PINO_PIR, INPUT);
  pinMode(PINO_LED, OUTPUT);
  digitalWrite(PINO_LED, LOW);
  conectarWiFi();
  mqttClient.setServer(MQTT_BROKER, MQTT_PORTA);
  conectarMqtt();
}

void loop() {
  if (WiFi.status() != WL_CONNECTED) {
    conectarWiFi();
  }
  if (!mqttClient.connected()) {
    conectarMqtt();
  }
  mqttClient.loop();

  bool emUso = digitalRead(PINO_PIR) == HIGH;
  digitalWrite(PINO_LED, emUso ? HIGH : LOW);

  unsigned long agora = millis();
  if (emUso != ultimoEstado || agora - ultimoEnvio >= INTERVALO_ENVIO_MS) {
    publicarLeitura(emUso);
    ultimoEstado = emUso;
    ultimoEnvio = agora;
  }

  delay(50);
}
