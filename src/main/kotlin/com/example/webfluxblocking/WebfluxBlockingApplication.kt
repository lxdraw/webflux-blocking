package com.example.webfluxblocking

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToFlux
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import reactor.blockhound.BlockHound
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@SpringBootApplication
class WebfluxBlockingApplication : ApplicationRunner {
    @Bean
    fun aRouter() = router {
        GET("/map") {
            Mono.empty<ServerResponse>().switchIfEmpty {
                ServerResponse.ok().bodyValue(TestResponse(mapOf("hello" to "world")))
            }
        }
    }

    data class TestResponse(val emptyMap: Map<String, String>)

    override fun run(args: ApplicationArguments?) {
        val webClient = WebClient.create()
        webClient.get().uri("http://localhost:8080/map").retrieve().bodyToMono(String::class.java).subscribe()
    }
}

fun main(args: Array<String>) {
    BlockHound.install()
    runApplication<WebfluxBlockingApplication>(*args)
}
