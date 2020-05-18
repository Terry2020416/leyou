package com.leyou.search.listener;

import com.leyou.search.service.IndexService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GoodsListener {

    @Autowired
    private IndexService indexService;

    //消费者
    //接收消息
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "q1",durable = "true"),
            exchange = @Exchange(value = "ly.item.exchange",type = ExchangeTypes.TOPIC),
            key = {"item.update","item.insert"}
    ))
    public void createIndex(Long id){
        //更新索引
        //有则修改，无则添加
        indexService.createIndex(id);
    }


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "q2",durable = "true"),
            exchange = @Exchange(value = "ly.item.exchange",type = ExchangeTypes.TOPIC),
            key = {"item.delete"}
    ))
    public void deleteIndex(Long id){
        //更新索引
        //有则修改，无则添加
        indexService.delIndex(id);
    }
}
