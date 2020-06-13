package jpabook.jpastore.service;

import jpabook.jpastore.domain.Address;
import jpabook.jpastore.domain.Member;
import jpabook.jpastore.domain.Order;
import jpabook.jpastore.domain.OrderStatus;
import jpabook.jpastore.domain.exception.NotEnoughStockException.NotEnoughStockException;
import jpabook.jpastore.domain.item.Book;
import jpabook.jpastore.domain.item.Item;
import jpabook.jpastore.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @PersistenceContext
    EntityManager em;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    @Test
    void 상품주문() throws Exception{
        //given
        Member member = createMember();
        Book book = createBook("JPA book", 10000, 10);
        //when
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        Order getOrder = orderRepository.findOne(orderId);
        assertEquals(OrderStatus.ORDER, getOrder.getStatus()); //주문시 상태는 order
        assertEquals(1,getOrder.getOrderItems().size()); //상품종류수가 일치
        assertEquals(10000 * orderCount, getOrder.getTotalPrice()); // 주문가격 = 가격 * 수량
        assertEquals(8, book.getStockQuantity()); //주문 수량만큼 재고가 줄어듦
    }

    @Test
    void 상품주문_재고수량초과() throws Exception{
        //given
        Member member = createMember();
        Item item = createBook("JPA book", 10000, 10);

        //when
        Throwable throwable = assertThrows(NotEnoughStockException.class, () -> {
            int orderCount = 11;
            orderService.order(member.getId(), item.getId(), orderCount);
            //then

        });

        //재고부족예외가 발생해야한다.
        assertEquals(NotEnoughStockException.class, throwable.getClass());
    }

    @Test
    void 주문취소() throws Exception{
        //given
        Member member = createMember();
        Item item = createBook("JPA book", 10000, 10);

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);
        //when
        orderService.cancelOrder(orderId);

        //then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.CANCEL, getOrder.getStatus());
        assertEquals(10, item.getStockQuantity());

    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("user1");
        member.setAddress(new Address("서울", "강남구", "1234"));
        em.persist(member);
        return member;
    }
}