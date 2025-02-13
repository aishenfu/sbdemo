package com.zark.sbproject.boot.start;

import com.zark.sbproject.boot.service.common.message.consumer.ActiveMqMessageListener;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;

@Configuration
@EnableJms
public class ActiveMqConfiguration {

    @Bean
    public ConnectionFactory connectionFactory() {
        return new ActiveMQConnectionFactory();
    }

    @Bean
    public JmsListenerContainerFactory<?> topicListenerFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();

        factory.setPubSubDomain(true);
        factory.setConnectionFactory(connectionFactory);

        return factory;
    }

    @Bean
    public JmsListenerContainerFactory<?> queueListenerFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();

        factory.setPubSubDomain(false);
        factory.setConnectionFactory(connectionFactory);

        return factory;
    }

    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
        return new JmsTemplate(connectionFactory);
    }

    @Bean
    public JmsMessagingTemplate jmsMessagingTemplate(JmsTemplate jmsTemplate) {
        return new JmsMessagingTemplate(jmsTemplate);
    }

    @Bean
    public DefaultMessageListenerContainer defaultMessageListenerContainer(ConnectionFactory connectionFactory) {
        DefaultMessageListenerContainer defaultMessageListenerContainer = new DefaultMessageListenerContainer();

        defaultMessageListenerContainer.setConnectionFactory(connectionFactory);
        defaultMessageListenerContainer.setMessageListener(new ActiveMqMessageListener());

//        Destination destination = new ActiveMQQueue("*");

        Destination destination = new ActiveMQTopic("*");

        defaultMessageListenerContainer.setDestination(destination);
//        defaultMessageListenerContainer.setMessageConverter(new ActiveMqConverter());

        return defaultMessageListenerContainer;
    }
}