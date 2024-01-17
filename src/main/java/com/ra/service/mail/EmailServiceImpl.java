package com.ra.service.mail;

import com.ra.model.dto.response.OrderDetailResponseDTO;
import com.ra.model.dto.response.OrdersResponseDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService{
    @Autowired
    private JavaMailSender javaMailSender;
    @Override
    public String sendMail(String email, OrdersResponseDTO ordersResponseDTO) {
        try{
            SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
            simpleMailMessage.setFrom("jav2306@gmail.com");
            simpleMailMessage.setTo("nguyenanhduc1101@gmail.com");
            simpleMailMessage.setText("thank you for checked out!!!");
            simpleMailMessage.setSubject("Online Shopping Mall");
            javaMailSender.send(simpleMailMessage);
            return "Send done !";


        }catch (Exception e){
            e.printStackTrace();
        }
       return null;
    }

//        try {
//            MimeMessage message = javaMailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message, true);
//
//            helper.setFrom("nguyenanhduc1101@gmail.com");
//            helper.setTo(email);
//            helper.setSubject("Order's Information");
//
//            // Tạo nội dung email
//            String emailContent = createEmailContent(ordersResponseDTO);
//
//            helper.setText(emailContent, true);
//
//            javaMailSender.send(message);
//
//            return "Email sent successfully";
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

//    private String createEmailContent(OrdersResponseDTO ordersResponseDTO) {
//        StringBuilder emailContentBuilder = new StringBuilder();
//        emailContentBuilder.append("<html><body>");
//        emailContentBuilder.append("<h2>Cảm ơn bạn đã đặt vé!</h2>");
//        emailContentBuilder.append("<h3>Chi tiết vé xem phim:</h3>");
//        emailContentBuilder.append("<table border='1' cellpadding='10'>");
//        emailContentBuilder.append("<tr><th>ID người đặt: </th><td>").append(ordersResponseDTO.getUserId()).append("</td></tr>");
//        emailContentBuilder.append("<tr><th>Chi tiết đơn hàng: </th><td>").append(ordersResponseDTO.getOrderDetailsId()).append("</td></tr>");
//        emailContentBuilder.append("<tr><th>Ngày đặt: </th><td>").append(ordersResponseDTO.getCreateAt()).append("</td></tr>");
//        emailContentBuilder.append("<tr><th>Địa điểm: </th><td>").append(ordersResponseDTO.getAddress()).append("</td></tr>");
//        emailContentBuilder.append("<tr><th>Tình trạng đơn hàng: </th><td>").append(ordersResponseDTO.getStatus()).append("</td></tr>");
////        emailContentBuilder.append("<tr><th>Thời gian</th><td>").append(ordersResponseDTO.getTimeSlotName()).append("</td></tr>");
////        emailContentBuilder.append("<tr><th>Giá vé</th><td>").append(ordersResponseDTO.getTotalAmount()).append("</td></tr>");
//        emailContentBuilder.append("</table>");
//        emailContentBuilder.append("</body></html>");
//        return emailContentBuilder.toString();
//    }
}

