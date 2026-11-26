#include <WiFi.h>
#include <PubSubClient.h>

const char* WIFI_SSID = "Wokwi-GUEST";
const char* WIFI_SENHA = "";
const int WIFI_CANAL = 6;

const char* MQTT_BROKER = "broker.hivemq.com";
const uint16_t MQTT_PORTA = 1883;
const char* MQTT_TOPICO = "fatec-dsm/smartgym/sensores/presenca";

const char* CODIGO_SENSOR = "SENSOR-001";

const uint8_t PINO_PIR = 13;
const uint8_t PINO_LED = 2;

const unsigned long INTERVALO_ENVIO_MS = 2000;
const unsigned long INTERVALO_RECONEXAO_MS = 3000;

WiFiClient wifiClient;
PubSubClient mqttClient(wifiClient);

char clientId[32];
bool ultimoEstado = false;
bool primeiraLeitura = true;
unsigned long ultimoEnvio = 0;
unsigned long ultimaTentativaMqtt = 0;

void conectarWiFi() {
  Serial.print("Conectando ao Wi-Fi ");
  Serial.print(WIFI_SSID);
  WiFi.mode(WIFI_STA);
  WiFi.setSleep(false);
  WiFi.setAutoReconnect(true);
  WiFi.begin(WIFI_SSID, WIFI_SENHA, WIFI_CANAL);
  while (WiFi.status() != WL_CONNECTED) {
    delay(100);
    Serial.print(".");
  }
  Serial.println();
  Serial.print("Wi-Fi conectado. IP: ");
  Serial.println(WiFi.localIP());
}

bool conectarMqtt() {
  Serial.print("Conectando ao broker MQTT ");
  Serial.print(MQTT_BROKER);
  Serial.print(" ... ");
  if (mqttClient.connect(clientId)) {
    Serial.println("conectado");
    primeiraLeitura = true;
    return true;
  }
  Serial.print("falhou, rc=");
  Serial.println(mqttClient.state());
  return false;
}

void publicarLeitura(bool emUso) {
  char mensagem[64];
  snprintf(mensagem, sizeof(mensagem), "{\"codigoSensor\":\"%s\",\"emUso\":%s}", CODIGO_SENSOR, emUso ? "true" : "false");
  if (mqttClient.publish(MQTT_TOPICO, mensagem)) {
    Serial.print("Publicado: ");
  } else {
    Serial.print("Falha ao publicar: ");
  }
  Serial.println(mensagem);
}

void setup() {
  Serial.begin(115200);
  pinMode(PINO_PIR, INPUT);
  pinMode(PINO_LED, OUTPUT);
  digitalWrite(PINO_LED, LOW);

  snprintf(clientId, sizeof(clientId), "smartgym-esp32-%08lx", (unsigned long)esp_random());

  conectarWiFi();

  mqttClient.setServer(MQTT_BROKER, MQTT_PORTA);
  mqttClient.setKeepAlive(15);
  mqttClient.setSocketTimeout(5);
  conectarMqtt();
}

void loop() {
  bool emUso = digitalRead(PINO_PIR) == HIGH;
  digitalWrite(PINO_LED, emUso);

  unsigned long agora = millis();

  if (WiFi.status() != WL_CONNECTED) {
    return;
  }

  if (!mqttClient.connected()) {
    if (agora - ultimaTentativaMqtt >= INTERVALO_RECONEXAO_MS) {
      ultimaTentativaMqtt = agora;
      conectarMqtt();
    }
    return;
  }

  mqttClient.loop();

  if (primeiraLeitura || emUso != ultimoEstado || agora - ultimoEnvio >= INTERVALO_ENVIO_MS) {
    publicarLeitura(emUso);
    ultimoEstado = emUso;
    ultimoEnvio = agora;
    primeiraLeitura = false;
  }

  delay(10);
}
