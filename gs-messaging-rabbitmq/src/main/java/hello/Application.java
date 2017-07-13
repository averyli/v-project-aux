package hello;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


/** We also will do some configuration here in the main class of SpringBoot */
@SpringBootApplication
public class Application {

    // constant with queue name, it will be used in many places 
	final static String queueName = "spring-boot";

    // creating a queue
	@Bean
    Queue queue() {
        return new Queue(queueName, false);
    }

	/* RabbitMQ type stuff - creating Topic and Binding of topic to queue: this are requirements of Spring AMQP */
	
	// here we will create exchange list
    @Bean
    TopicExchange exchange() {
        return new TopicExchange("spring-boot-exchange");
    }
    // binds queue and exchange list
    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(queueName);
    }

    
    /* Now we're gonna create listener container */
    
    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
            MessageListenerAdapter listenerAdapter) {
    	
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    // Creating listener adapter for register Receiver (=MessageListener: any POJO class from queue) in container
    // As Receiver is a bean (it is marked by @Component annotation) it will be injected here!!!
    @Bean
    MessageListenerAdapter listenerAdapter(Receiver receiver) {
        // Adapter is kind of a wrapper around simple POJO
    	// and we tell him the default listener's method name - 'receiveMessage' !!! It decouples implementation from RabbitMQ
    	return new MessageListenerAdapter(receiver, "receiveMessage");
    }

    // Let's rock
    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(Application.class, args);
    }

}
