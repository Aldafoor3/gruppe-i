package com.example.demo;

import com.example.demo.ChatBot.ChatbotController;
import com.example.demo.ChatBot.ChatbotService;
import com.example.demo.DiscussionForum.CommentRepository;
import com.example.demo.DiscussionForum.DiscussionService;
import com.example.demo.DiscussionForum.DiscussionTopicController;
import com.example.demo.DiscussionForum.DiscussionTopicRepository;
import com.example.demo.SupportTicket.Ticket;
import com.example.demo.SupportTicket.TicketController;
import com.example.demo.SupportTicket.TicketService;
import com.example.demo.email.EmailService;
import com.example.demo.login.LoginService;
import com.example.demo.testChat.*;
import com.example.demo.user.UserRepository;
import com.example.demo.user.Users;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class BackendSepsepApplicationTests {
	private static ChatService chatService;
	private static LoginService loginService;
	private static UserRepository userRepository;
	private static ChatRoom_Repository chatRoomRepository;
	private static ChatController chatController;
	private static Message_Repository messageRepository;
	private static TicketService ticketService;
	private static TicketController ticketController;
	private static ChatbotService chatbotService;
	private ChatbotController chatbotController;
	private static DiscussionService discussionService;
	private static DiscussionTopicRepository topicRepository;
	private static CommentRepository commentRepository;
	private static EmailService emailService;

	@BeforeAll
	public static void setupTest() {
		chatService = mock(ChatService.class);
		loginService = mock(LoginService.class);
		userRepository = mock(UserRepository.class);
		chatRoomRepository = mock(ChatRoom_Repository.class);
		messageRepository = mock(Message_Repository.class);
		chatController = new ChatController(chatService);
		ticketService = mock(TicketService.class);
		ticketController = new TicketController(ticketService);
		chatbotService = mock(ChatbotService.class);
		discussionService = mock(DiscussionService.class);
		topicRepository = mock(DiscussionTopicRepository.class);
		commentRepository = mock(CommentRepository.class);
		emailService = mock(EmailService.class);
	}

	@BeforeEach
	public void setup() {
		chatbotController = new ChatbotController();
		chatbotController.setChatbotService(chatbotService);
	}

	@Test
	public void testSendMessage() {
		Users user = new Users();
		user.setId(1L);
		when(loginService.getUserBySessionID(anyString())).thenReturn(user);

		Message_Instance message = new Message_Instance();
		message.setChatRoomId(1L);
		message.setMessageId(1L);
		message.setRead(true);
		when(messageRepository.findById(message.getMessageId())).thenReturn(Optional.of(message));

		ChatRoom_Instance chatRoom = new ChatRoom_Instance();
		when(chatRoomRepository.findById(chatRoom.getChatRoomId())).thenReturn(Optional.of(chatRoom));

		when(chatService.addNewMsg(eq("1"), anyLong(), eq("message updated"))).thenReturn(true);

		boolean result = chatController.sendMsg(1L, "1", "message updated").getStatusCode().is2xxSuccessful();
		boolean expected = true;
		assertEquals(expected, result);
	}

	@Test
	public void testSendTicket() {
		// Prepare test data
		String sessionID = "session123";
		String description = "Sample ticket description";

		Ticket ticket = new Ticket();
		ticket.setEmail("test@example.com");
		ticket.setDescription(description);

		when(ticketService.sendTicket(any(Ticket.class), eq(sessionID))).thenReturn(true);

		// Perform the API request
		ResponseEntity<String> responseEntity = ticketController.sendTicket(sessionID, description);

		// Assert the response
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals("Ticket erfolgreich gesendet", responseEntity.getBody());
	}

	@Test
	public void testUpdateTicketStatus() {
		// Prepare test data
		Long ticketID = 123L;
		String sessionID = "session456";

		when(ticketService.updateTicketStatus(eq(ticketID), eq(sessionID))).thenReturn(true);

		// Perform the API request
		ResponseEntity<String> responseEntity = ticketController.updateTicketStatus(ticketID, sessionID);

		// Assert the response
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals("Ticket-Status erfolgreich aktualisiert", responseEntity.getBody());
	}

	@Test
	public void testCountUsers() {
		int userCount = 10;
		when(chatbotService.getUserCount()).thenReturn(userCount);

		ResponseEntity<Integer> responseEntity = chatbotController.countUsers();

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(userCount, responseEntity.getBody());
	}

	@Test
	public void testCountAdmins() {
		int adminCount = 5;
		when(chatbotService.getAdminCount()).thenReturn(adminCount);

		ResponseEntity<Integer> responseEntity = chatbotController.countAdmins();

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(adminCount, responseEntity.getBody());
	}

	@Test
	public void testCountTickets() {
		int ticketCount = 7;
		when(chatbotService.getTicketCount()).thenReturn(ticketCount);

		ResponseEntity<Integer> responseEntity = chatbotController.countTickets();

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(ticketCount, responseEntity.getBody());
	}

	@Test
	public void testCreateTopic() {
		// Prepare test data
		String topic = "Test Topic";
		String category = "Test Category";
		String text = "Test Text";
		String sessionId = "test-session-id";

		Users user = new Users();
		user.setId(1L);
		when(loginService.getUserBySessionID(sessionId)).thenReturn(user);

		DiscussionTopicController discussionTopicController = new DiscussionTopicController();
		discussionTopicController.setDiscussionService(discussionService);

		// Perform the API request
		ResponseEntity<String> responseEntity = discussionTopicController.createTopic(topic, category, text, sessionId);

		// Assert the response
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals("topic created or not created", responseEntity.getBody());

		// Verify the service method was called
		verify(discussionService).createTopic(topic, category, text, sessionId);
	}



}


