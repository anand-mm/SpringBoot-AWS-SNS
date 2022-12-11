package com.simple.aws.sns.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.simple.aws.sns.model.Notification;

@RestController
@RequestMapping("/aws")
public class awsSnsController {

    @Autowired
    private AmazonSNSClient snsClient;

    @Value("${topic.arn.sms}")
    private String TOPIC_ARN_SMS;

    @Value("${topic.arn.email}")
    private String TOPIC_ARN_EMAIL;

    @GetMapping("/addSubscriptionforsms/{phone}")
    public String addSubscriptionforsms(@PathVariable String phone) {
        SubscribeRequest request = new SubscribeRequest(TOPIC_ARN_SMS, "sms", phone);
        snsClient.subscribe(request);
        return "Subscription request is pending. To confirm the subscription, check your email : " + phone;
    }

    @GetMapping("/addSubscriptionforemail/{email}")
    public String addSubscriptionforemail(@PathVariable String email) {
        SubscribeRequest request = new SubscribeRequest(TOPIC_ARN_EMAIL, "Email", email);
        snsClient.subscribe(request);
        return "Subscription request is pending. To confirm the subscription, check your email : " + email;
    }

    @PostMapping("/sendNotification")
    public String publishMessageToTopic(@RequestBody Notification notification) {
        PublishRequest publishRequest = new PublishRequest(TOPIC_ARN_EMAIL, notification.getMessage(),
                notification.getSubject());
        snsClient.publish(publishRequest);
        return "Notification send successfully !!";
    }

    @GetMapping("/sendNotificationautomatic")
    public String publishMessageToTopic() {
        PublishRequest publishRequest = new PublishRequest(TOPIC_ARN_EMAIL, buildEmailBody(),
                "Notification: Network connectivity issue");
        snsClient.publish(publishRequest);
        return "Notification send successfully !!";
    }

    private String buildEmailBody() {
        return "Dear Employee ,\n" +
                "\n" +
                "\n" +
                "Connection down Bangalore." + "\n" +
                "All the servers in Bangalore Data center are not accessible. We are working on it ! \n" +
                "Notification will be sent out as soon as the issue is resolved. For any questions regarding this message please feel free to contact IT Service Support team";
    }

}
