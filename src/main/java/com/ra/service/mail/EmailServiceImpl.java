package com.ra.service.mail;

import com.ra.model.dto.response.OrderDetailResponseDTO;
import com.ra.model.dto.response.OrdersResponseDTO;
import com.ra.model.entity.OrderDetail;
import com.ra.model.entity.Orders;
import com.ra.model.entity.User;
import com.ra.repository.OrderDetailRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Override
    public String sendMail(String email, Orders orders) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom("nguyenanhduc1101@gmail.com");
            simpleMailMessage.setTo(orders.getUser().getEmail());
            simpleMailMessage.setText("Thank you for checked out. Your order is waiting to be processed. Please wait for several days.");
            simpleMailMessage.setSubject("Online Shopping Mall");
            javaMailSender.send(simpleMailMessage);
            return "Send done !";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String sendAcceptMail(Orders orders) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("nguyenanhduc1101@gmail.com");
            helper.setTo(orders.getUser().getEmail());
            helper.setSubject("Order's Information");

            // Tạo nội dung email
            String emailContent = createEmailContent(orders);

            helper.setText(emailContent, true);

            javaMailSender.send(message);

            return "Email sent successfully";
        } catch (MessagingException e) {
            e.printStackTrace();
            return "Sent fail!!!";
        }
    }

    @Override
    public String sendDenyMail(Orders orders) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom("nguyenanhduc1101@gmail.com");
            simpleMailMessage.setTo(orders.getUser().getEmail());
            simpleMailMessage.setText("Sorry. Your order has been denied !!!");
            simpleMailMessage.setSubject("Online Shopping Mall");
            javaMailSender.send(simpleMailMessage);
            return "Send done !";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String createEmailContent(Orders orders) {
        StringBuilder emailContentBuilder = new StringBuilder();
            emailContentBuilder.append("<html><body>");
            emailContentBuilder.append("<h2>Cảm ơn bạn đã đặt hàng!</h2>");
            emailContentBuilder.append("<h3>Chi tiết đơn hàng:</h3>");
            emailContentBuilder.append("<table border='1' cellpadding='10'>");
            emailContentBuilder.append("<tr><th>ID người đặt: </th><td>").append(orders.getUser().getId()).append("</td></tr>");
            emailContentBuilder.append("<tr><th>Ngày đặt: </th><td>").append(orders.getCreateAt()).append("</td></tr>");
            emailContentBuilder.append("<tr><th>Địa điểm: </th><td>").append(orders.getAddress()).append("</td></tr>");
            emailContentBuilder.append("<tr><th>Tình trạng đơn hàng: </th><td>").append(orders.getStatus()).append("</td></tr>");
            DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
            String formattedAmount = decimalFormat.format(orders.getTotal());
            emailContentBuilder.append("<tr style=\"background-color: #f2f2f2;\"><th style=\"text-align: left;\">Total</th><td>").append(formattedAmount).append(" VND</td></tr>");
            List<OrderDetail> orderDetailsList = orderDetailRepository.findAllByOrders_Id(orders.getId());
            if (orderDetailsList != null) {
                for (OrderDetail orderDetail : orderDetailsList) {
                    emailContentBuilder.append("<tr style=\"background-color: #f2f2f2;\"><th style=\"text-align: left;\">ProductName</th><td>").append(orderDetail.getProduct().getProductName()).append("</td></tr>");
                    emailContentBuilder.append("<tr><th style=\"background-color: #f2f2f2; text-align: left;\">Quantity</th><td>").append(orderDetail.getQuantity()).append("</td></tr>");// Thêm các thông tin khác tùy thuộc vào cấu trúc của OrderDetail
                }
            }
            emailContentBuilder.append("</table>");
            emailContentBuilder.append("</body></html>");
        return emailContentBuilder.toString();
    }
}

