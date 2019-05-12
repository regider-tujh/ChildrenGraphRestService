package egor.timoshenko.configuration

import egor.timoshenko.graph.RuntimeChildrenGraph
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ChildrenGraphConfiguration {

    @Bean
    fun childrenGraph() = RuntimeChildrenGraph()
}