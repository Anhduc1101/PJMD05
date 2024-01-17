package com.ra.service.cart;

import com.ra.exception.CustomException;
import com.ra.model.dto.request.CartItemRequestDTO;
import com.ra.model.dto.request.CartRequestDTO;
import com.ra.model.dto.response.CartItemResponseDTO;
import com.ra.model.dto.response.CartResponseDTO;
import com.ra.model.dto.response.OrdersResponseDTO;
import com.ra.model.dto.response.UserResponseDTO;
import com.ra.model.entity.*;
import com.ra.repository.*;
import com.ra.service.cartItem.CartItemService;
import com.ra.service.mail.EmailService;
import com.ra.service.orders.OrdersService;
import com.ra.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private EmailService emailService;

    @Override
    public Page<CartResponseDTO> getAll(Pageable pageable) {
        Page<Cart> cartPage = cartRepository.findAll(pageable);
        return cartPage.map(CartResponseDTO::new);
    }

    @Override
    public List<CartResponseDTO> findAll() {
        List<Cart> cartList = cartRepository.findAll();
        return cartList.stream().map(CartResponseDTO::new).toList();
    }


    @Override
    public CartResponseDTO findById(Long id) {
        Optional<Cart> cartOptional = cartRepository.findById(id);
        return cartOptional.map(CartResponseDTO::new).orElse(null);
    }


    @Override
    public void addToCart(Long id, CartItemRequestDTO cartItemRequestDTO) throws CustomException {
        User user = userRepository.findById(id).orElseThrow(() -> new CustomException("User is not found"));
        Cart cart = cartRepository.findByUser(user);
        Product product = productRepository.findById(cartItemRequestDTO.getProductId()).orElseThrow(() -> new CustomException("Product is not found"));

        if (cart == null) {
            Cart newCart = new Cart();
            newCart.setUser(user);
            cartRepository.save(newCart);
        }
        CartItem cartItem;
        if (cartItemRepository.existsCartItemByCartAndProduct(cart, product)) {
            cartItem = cartItemRepository.findByCartAndProduct(cart, product);
            cartItem.setQuantity(cartItem.getQuantity() + cartItemRequestDTO.getQuantity());
        } else {
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setQuantity(cartItemRequestDTO.getQuantity());
            cartItem.setPrice(product.getPrice());
            cartItem.setProduct(product);
        }
        cartItemRepository.save(cartItem);
    }


    @Override
    public void clearAllCartItems() {
        cartItemRepository.deleteAll();
    }

    @Override
    public void clearAllCartItemsByUser(Cart cart) {
        cart.getCartItem().clear();
        cartRepository.save(cart);
    }

    @Override
    public void placeOrder(User user) {
        Cart cart = cartRepository.findByUser(user);
        List<CartItem> cartItemList = cartItemRepository.findAllByCart(cart);
        Orders orders = new Orders();
        orders.setUser(user);
        orders.setCreateAt(new Date());
        ordersRepository.save(orders);

        for (CartItem item : cartItemList) {
            OrderDetail od = new OrderDetail();
            od.setProduct(item.getProduct());
            od.setQuantity(item.getQuantity());
            od.setPrice(item.getPrice());
            od.setOrders(orders);
            orderDetailRepository.save(od);
        }
        orders.setAddress(user.getAddress());
//        orders.setNote(orders.getNote());
        orders.setPhone(user.getPhone());
        float total = cartItemList.stream().map(cartItem -> cartItem.getPrice() * cartItem.getQuantity()).reduce(Float::sum).orElse(0f);
        orders.setTotal(total);
        ordersRepository.save(orders);
//        emailService.sendMail(user.getEmail(),new OrdersResponseDTO(orders));
    }


}
