package com.smartgym.backend.mqtt

import com.smartgym.backend.dto.LeituraSensorMensagem
import com.smartgym.backend.service.LeituraSensorService
import jakarta.annotation.PreDestroy
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttAsyncClient
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import tools.jackson.databind.json.JsonMapper
import java.util.UUID
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@Component
class SensorMqttListener(
    private val leituraSensorService: LeituraSensorService,
    private val jsonMapper: JsonMapper,
    @Value("\${mqtt.broker-url}") private val brokerUrl: String,
    @Value("\${mqtt.topico}") private val topico: String,
    @Value("\${mqtt.client-id}") private val clientIdBase: String
) : MqttCallbackExtended {

    private val log = LoggerFactory.getLogger(SensorMqttListener::class.java)
    private val agendador = Executors.newSingleThreadScheduledExecutor()
    private lateinit var cliente: MqttAsyncClient

    private val opcoes = MqttConnectOptions().apply {
        isAutomaticReconnect = true
        isCleanSession = true
        connectionTimeout = 10
        keepAliveInterval = 30
    }

    @EventListener(ApplicationReadyEvent::class)
    fun iniciar() {
        val clientId = "$clientIdBase-${UUID.randomUUID().toString().take(8)}"
        cliente = MqttAsyncClient(brokerUrl, clientId, MemoryPersistence())
        cliente.setCallback(this)
        conectar()
    }

    private fun conectar() {
        try {
            cliente.connect(opcoes, null, object : IMqttActionListener {
                override fun onSuccess(token: IMqttToken) {}

                override fun onFailure(token: IMqttToken?, erro: Throwable?) {
                    log.warn("Falha ao conectar no broker MQTT {}: {}", brokerUrl, erro?.message)
                    agendarNovaTentativa()
                }
            })
        } catch (e: MqttException) {
            log.warn("Erro ao conectar no broker MQTT {}: {}", brokerUrl, e.message)
            agendarNovaTentativa()
        }
    }

    private fun agendarNovaTentativa() {
        if (!agendador.isShutdown) {
            agendador.schedule({ conectar() }, 5, TimeUnit.SECONDS)
        }
    }

    override fun connectComplete(reconnect: Boolean, serverURI: String) {
        log.info("Conectado ao broker MQTT {} (reconexao: {})", serverURI, reconnect)
        try {
            cliente.subscribe(topico, 1)
            log.info("Inscrito no topico {}", topico)
        } catch (e: MqttException) {
            log.error("Erro ao assinar o topico {}: {}", topico, e.message)
        }
    }

    override fun messageArrived(topic: String, message: MqttMessage) {
        val conteudo = String(message.payload, Charsets.UTF_8)
        try {
            val mensagem = jsonMapper.readValue(conteudo, LeituraSensorMensagem::class.java)
            val codigoSensor = mensagem.codigoSensor?.trim()
            val emUso = mensagem.emUso
            if (codigoSensor.isNullOrEmpty() || emUso == null) {
                log.warn("Mensagem MQTT ignorada (campos obrigatorios ausentes): {}", conteudo)
                return
            }
            val leitura = leituraSensorService.registrar(codigoSensor, emUso)
            log.info("Leitura gravada: sensor={} emUso={} equipamentoId={}", leitura.codigoSensor, leitura.emUso, leitura.equipamentoId)
        } catch (e: Exception) {
            log.warn("Erro ao processar mensagem MQTT '{}': {}", conteudo, e.message)
        }
    }

    override fun connectionLost(causa: Throwable?) {
        log.warn("Conexao com o broker MQTT perdida: {}", causa?.message)
    }

    override fun deliveryComplete(token: IMqttDeliveryToken?) {}

    @PreDestroy
    fun encerrar() {
        agendador.shutdownNow()
        if (!this::cliente.isInitialized) return
        try {
            if (cliente.isConnected) {
                cliente.disconnect().waitForCompletion(2000)
            }
            cliente.close()
        } catch (e: MqttException) {
            log.warn("Erro ao encerrar o cliente MQTT: {}", e.message)
        }
    }
}
