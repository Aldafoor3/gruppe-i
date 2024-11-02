package com.example.demo;

//import com.example.demo.SupportTicket.TicketRepository;
//import com.example.demo.SupportTicket.TicketService;
import com.example.demo.SupportTicket.Ticket;
import com.example.demo.SupportTicket.TicketRepository;
import com.example.demo.admin.Admin;
import com.example.demo.admin.AdminRepository;
import com.example.demo.email.EmailService;
import com.example.demo.login.SessionRepository;
import com.example.demo.login.Sessions;
import com.example.demo.user.UserRepository;
import com.example.demo.user.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
        import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ConfigurableApplicationContext;
        import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
        import org.springframework.web.filter.CorsFilter;

@SpringBootApplication
public class BackendSepsepApplication {

	@Autowired
	private EmailService emailService;

//	public static void main(String[] args) {
//		SpringApplication.run(BackendSepsepApplication.class, args);
//	}

	@EventListener(ApplicationReadyEvent.class)
	public void sendMail(){
		/*emailService.sendEmail("gruppe.i1234@gmail.com",
				"Verifiziere dein Email Account",
				"Über diesen Link kannst du dein Account verifizieren");
		*/
	}


//	CommandLineRunner inti (SessionRepository sessionRepository)
//	{
//		return args -> {
//			sessionRepository.save(new Sessions(, true, 1));
//
//		}
//	}

	@Bean
	CommandLineRunner init(AdminRepository adminRepository, UserRepository userRepository)
	{
		return args ->
		{
			adminRepository.save(new Admin("Max","Mustermann","Mustermann@gmail.com", "sicheresPasswort"));
			adminRepository.save(new Admin("a","a","a@a.a", "a"));
			adminRepository.save(new Admin("sep", "sep","septestmail@gmx.de", "gruppei123"));

			Users newUser = new Users("a","a","a@a.a", "a", "a");
			newUser.setProfilePicture(standardImage());
			userRepository.save(newUser);

			newUser = new Users("sep", "sep","septestmail@gmx.de", "gruppei123", "01.01.2000" );
			newUser.setProfilePicture(standardImage());
			userRepository.save(newUser);

			Users newUser2 = new Users("sepsep", "sepsep", "sepseptestmail@gmx.de", "gruppei123", "01.01.2000");
			newUser2.setProfilePicture(standardImage());
			userRepository.save(newUser2);
			//gruppei123
			//septestmail@gmx.de
		};
	}

	@Autowired
	private CorsConfig config;

	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilter() {
		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(config.corsConfigurationSource()));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return bean;
	}
//private final TicketService ticketService;
//
//	@Autowired
//	public BackendSepsepApplication(TicketService ticketService) {
//		this.ticketService = ticketService;
//	}

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(BackendSepsepApplication.class, args);

		//Get the DemoApplication bean
		BackendSepsepApplication demoApplication = context.getBean(BackendSepsepApplication.class);
//
//		// Create a new ticket
//		String description = "Incorrect data sets";
//		String email = "meftehikhalil78@gmail.com" ;
//		Ticket ticket = demoApplication.createTicket(description , email);
//
//		// Print the ticket details
//		System.out.println("Ticket created:");
//		System.out.println("ID: " + ticket.getId());
//		System.out.println("Description: " + ticket.getDescription());
//		System.out.println("Status: " + ticket.getStatus());
//		System.out.println("Email: " + ticket.getEmail());
	}

//	public Ticket createTicket(String description , String email) {
//		// Open a new ticket
//		TicketRequest ticketRequest = new TicketRequest();
//		ticketRequest.setDescription("Incorrect data set");
//		ticketRequest.setEmail("meftehikhalil78@gmail.com");
//		Ticket ticket = ticketService.createTicket(ticketRequest );
//		return ticket;
//	}
//	public void resolveTicket(Long ticketId) {
//		// Resolve the ticket
//		Ticket resolvedTicket = ticketService.resolveTicket(ticketId);
//
//		// Print the resolved ticket details
//		System.out.println("Ticket resolved:");
//		System.out.println("ID: " + resolvedTicket.getId());
//		System.out.println("Description: " + resolvedTicket.getDescription());
//		System.out.println("Status: " + resolvedTicket.getStatus());
//		System.out.println("Email: " + resolvedTicket.getEmail());
//	}

	public byte[] standardImage(){
		return new byte[]{-119, 80, 78, 71, 13, 10, 26, 10, 0, 0, 0, 13, 73, 72, 68, 82, 0, 0, 1, 33, 0, 0, 1, 33, 8, 3, 0, 0, 0, 7, -61, -26, 41, 0, 0, 0, 32, 99, 72, 82, 77, 0, 0, 122, 38, 0, 0, -128, -124, 0, 0,
				-6, 0, 0, 0, -128, -24, 0, 0, 117, 48, 0, 0, -22, 96, 0, 0, 58, -104, 0, 0, 23, 112, -100, -70, 81, 60, 0, 0, 1, 107, 80, 76, 84, 69, -7, -70, 10, -55, -100, 28, 91, 89, 68, 55, 66, 81, 67,
				74, 77, -128, 111, 54, -31, -85, 19, -68, -107, 32, -104, 126, 46, -19, -77, 14, -92, -122, 41, -80, -115, 37, 79, 81, 72, 104, 96, 63, -116, 119, 50, -43, -92, 23, 116, 104, 59, 79, 89, 102
				, -70, -66, -59, -10, -9, -7, -58, -54, -49, 103, 111, 123, 91, 100, 113, -22, -20, -17, -105, -99, -91, 67, 77, 92, 127, -122, -112, -117, -111, -101, -34, -32, -28, -94, -88, -80, -46, -43
				, -38, -82, -77, -70, 115, 123, -122, -10, -83, 17, -11, -88, 19, -13, -97, 24, -14, -106, 29, -16, -115, 33, -18, -124, 38, -20, 123, 42, -22, 114, 47, -8, -74, 12, -17, -119, 35, -15, -110
				, 31, -21, 119, 45, -9, -79, 15, -14, -101, 26, -19, -128, 40, -12, -92, 22, 100, 78, 73, -89, 96, 60, 77, 72, 77, -100, 93, 62, -44, 108, 51, 66, 69, 79, 122, 84, 68, -67, 102, 56, -33, 111
				, 49, -123, 87, 66, -11, -26, -32, -17, -92, 123, -13, -42, -57, -19, -109, 98, -18, -100, 110, -11, -17, -20, -16, -75, -108, -15, -67, -95, -21, 122, 60, -20, -125, 72, -14, -51, -70, -12,
				-34, -45, -20, -117, 85, -14, -59, -83, -66, 110, 68, -56, 105, 53, 89, 75, 75, -70, -121, 39, -49, 102, 43, -76, 90, 40, -63, 96, 41, -36, 108, 45, -117, 72, 34, 71, 41, 25, 17, 17, 17, 58
				, 35, 23, 112, 59, 30, 98, 53, 28, 44, 29, 21, 31, 23, 19, 85, 47, 26, -103, 78, 36, 126, 66, 32, -90, 84, 38, -8, -55, 70, -17, -84, -121, -8, -59, 55, -7, -62, 40, -9, -32, -97, -10, -13,
				-22, -7, -66, 25, -8, -39, -126, -8, -43, 115, -10, -17, -37, -8, -47, 100, -9, -24, -67, -56, 105, 54, -123, 87, 67, 55, 66, 82, 122, 84, 69, -9, -28, -82, 111, 81, 71, -111, 90, 65, 77, 72
				, 78, 66, 69, 80, -82, -85, -82, 79, 89, 103, -105, -99, -90, 103, 111, 124, 127, -122, -111, -12, -65, 109, -1, -1, -1, 113, 49, 56, 49, 0, 0, 0, 1, 98, 75, 71, 68, 120, -42, -37, -28, 70,
				0, 0, 0, 7, 116, 73, 77, 69, 7, -25, 6, 30, 14, 52, 54, -122, 29, 31, -14, 0, 0, 0, 1, 111, 114, 78, 84, 1, -49, -94, 119, -102, 0, 0, 15, 125, 73, 68, 65, 84, 120, -38, -19, -99, -21, 67, -
				44, 56, 23, -58, -63, -114, 8, 2, 94, -24, 56, 44, -54, 69, -18, 32, 32, 10, -120, -117, -128, -62, -126, -32, -22, -118, 10, 42, -88, -69, -82, -70, 23, 87, -57, 125, 111, 123, -5, -9, -33,
				-71, -64, -48, 54, 57, -55, 73, 114, -38, -76, 37, -49, 55, -127, -55, 52, 63, -109, -25, -100, -100, -76, 105, 83, -109, -109, -109, -109, -109, -109, -109, -109, -109, -109, -109, -109, -
				109, -109, -109, -109, -109, -109, -109, -109, -109, -109, -109, 83, -54, -43, 124, -54, -13, 10, -89, 91, 108, 95, 70, 122, -44, 114, -38, -13, -68, 83, -51, -115, 127, -97, -15, -22, 106,
				109, -77, 125, 101, -23, 80, 91, -21, 33, -112, 51, 71, 63, 41, 28, -2, -64, 43, 56, 70, 85, 62, -57, 60, 14, 127, -44, -20, 29, -85, 112, -42, -10, 5, -38, -42, -39, 66, 0, -57, -31, 60, 107, -9, -126, -22, 104, -73, 125, -115, 54, -43, -34, 17, -126, 113, -56, -94, -43, 11, -85, -77, -39, -20, 91, -78, -85, -26, -50, 8, -118, 86, 62, -95, 10, -93, 19, 25, -42, 90, 58, 25, 16,
				-121, -124, -50, 121, -84, -50, -99, 56, -53, 110, -29, 98, -88, -1, -82, -109, -13, -85, 74, 88, -77, 125, -55, -55, -86, -75, -64, -93, -48, 41, 32, 84, -79, -20, 19, 100, 71, -51, 29, 124, 6, 98, 66, 39, -57, -114, 90, 96, 2, -11, 63, -24, -16, 64, -99, -124, 12, -78, -83, 21, -18, 127, 71, -3, 79, 60, -127, 10, -71, -49, -114, -38, 11, -94, -2, -53, 9, 85, 6, -38, 121, -37
				, 125, -120, 83, -25, 59, -59, -67, -57, 16, -54, 115, -28, -25, 70, 120, -106, 80, -117, -20, -81, -68, -114, 51, -122, 87, -110, 82, -99, -23, -112, 118, -67, 22, -85, -102, -91, 127, -106
				, -49, -88, -42, -46, -119, -24, 120, 51, -106, 80, 14, -41, -4, 103, 11, -104, 126, -29, 9, 121, -34, -87, 92, 57, -10, -7, 83, -72, 94, -85, 16, -54, 83, 114, 36, 74, -127, 56, -124, -38,
				-79, 127, -99, -101, 117, 72, -77, -36, -95, -113, 84, 75, 7, -47, 60, -67, 124, 4, 126, 121, -120, 15, -50, 27, 85, 66, 57, 24, 70, 10, 3, 72, -117, 80, -42, -121, -111, -46, 0, -46, 36, -108, -23, -96, -122, 13, 97, 102, -124, 26, -75, -55, -20, 73, -77, -85, -22, 31, -13, 78, 101, 50, -59, 110, 81, 29, 64, 71, -124, 46, -88, 127, 46, -109, 69, -111, 118, 84, 18, 29, -47, -123, -22, 39, 49, -53, 19, -50, 71, 51, 102, -40, 109, 58, 3, -31, -80, -56, -88, 71, -56, -21, -56, -108, 97, -97, 87, -118, -15, 52, -124, 60, 47, 67, -117, -39, -77, -70, 125, 4, 9, 93, -20
				, -14, 125, -65, 120, 73, -14, -15, -84, -52, 52, -23, 12, -69, 84, -84, 116, -73, -21, -94, 2, -95, 82, -73, 95, 19, -9, 67, 1, 101, 99, -90, -55, 102, 88, 109, 56, 84, -44, 93, -62, 19, 42
				, -6, 71, -6, -86, 71, -36, 122, 6, 98, -102, 100, 101, -34, -13, 85, -93, -77, 69, 52, -95, -110, 127, -84, -18, -53, -30, 47, 56, -99, -14, -103, -42, 118, 90, 124, -3, -105, -69, 3, -99,
				101, 7, 17, 64, -24, -110, 31, 84, 81, 60, -43, -46, -99, 61, 74, -78, -60, -117, -59, 80, 87, 89, -29, 5, 8, 93, -15, -61, 18, 79, -75, 66, -118, -105, -5, -51, -62, 44, 49, 48, -63, -22, -
				70, -126, 37, 20, -3, -96, -33, 43, -114, 106, -87, 53, 35, -79, 5, 93, -22, -115, -10, -13, 43, 109, 66, -66, -33, 39, 28, 70, -89, 109, -93, -32, 75, 104, 65, 61, 125, 108, 47, 77, 8, 73,
				-122, 81, 103, 10, -3, -70, 77, -104, 10, -77, 3, -56, -108, -112, 100, 24, -91, -49, -81, -123, 30, -51, 27, 64, -26, -124, -60, -61, -88, -112, -78, -28, -15, -68, -56, -93, -71, 3, -120,
				-128, 16, -81, -123, 0, -94, 84, -7, -75, -80, -44, -95, -48, 63, 85, 66, 126, 127, 73, -16, -59, 41, 66, 36, 10, 98, -91, 126, 63, 70, 66, -30, 20, 59, 53, -117, 125, -47, 82, 62, -108, 68,
				-57, 64, -88, -78, -102, 21, 24, 118, 74, -94, -66, 32, -54, -9, 116, -119, -6, 70, 67, -56, -17, 21, -52, -76, 84, 32, 18, 0, 42, -11, -6, 9, 16, -30, 37, -25, 105, 66, 36, 0, 116, 69, -46
				, 49, -128, 16, -69, -41, 33, 35, -28, 15, -64, 51, -51, 58, 34, 24, 80, -49, -128, -84, 95, 44, 33, 96, 55, 72, 74, 72, 20, -45, 44, 35, -126, 1, 9, 98, 88, 12, -124, -4, -18, -85, -23, 68,
				4, 3, -70, -38, 45, -17, 21, 37, 33, 81, -10, 104, 17, 17, 12, 72, -77, 79, 38, -124, 4, 102, -92, -126, 104, 112, 104, 120, 100, 100, 116, 108, 108, 108, 124, 34, -92, -15, -54, -113, 70,
				71, 70, -122, -121, 6, 41, 0, -55, 45, 40, 14, 66, 126, -65, 9, -94, -55, 10, -103, 107, 99, 19, 40, -115, 93, -85, -112, -102, 52, 1, 36, -73, 32, 17, -95, 51, -52, -113, -111, -124, -72, -
				101, 3, 117, 9, -77, -21, -63, -31, -87, -79, 105, 28, -101, -96, -90, -57, -90, -122, -123, 3, 10, -52, -92, 75, 8, 11, 2, 8, -43, -18, -109, 102, -17, 99, -108, -91, 13, -57, -120, 64, -65
				, 6, -42, 104, 51, 21, 56, -22, 108, 66, -29, 105, 106, 120, -122, -33, 54, -72, 22, -61, 120, 116, 93, -20, -110, 10, -72, -45, -13, 18, -74, 69, 94, -101, -63, -90, -61, 67, -25, -6, -75,
				113, 51, 58, 13, -113, -70, 118, -99, 29, 76, -32, 13, -85, -105, -15, -67, -71, -60, -17, -122, 17, 33, -65, 15, -72, -82, 112, -67, 104, 102, 120, -108, -120, 78, 99, -50, -115, -122, -57,
				18, 88, 15, -22, 83, -24, 12, 64, -120, 125, 106, 65, -123, -112, 63, 0, 92, 89, 71, -93, 48, 59, 56, 53, 75, 75, -25, 72, -77, 83, -115, -95, -44, 6, -19, -85, -30, -126, 24, 72, -88, 94,
				58, 53, 35, 4, 34, -86, 63, -67, 118, 99, 84, -61, -108, 85, -122, -46, -115, -38, -41, 64, 53, 105, 37, 64, 28, 66, 77, 124, 66, -98, 82, -77, 126, 17, -120, -6, -25, -30, -58, 115, 12, 9,
				-72, 127, -77, -89, -88, -42, 19, -74, 5, 26, 66, 64, 98, 116, 115, 46, 1, 60, -121, -112, -26, 110, 114, 1, 33, -45, 32, 41, 33, 118, -124, 42, 54, -52, 65, 52, -65, 112, 43, 41, 60, 117, -
				35, 90, -104, 55, 6, -60, 18, 2, -97, 4, 86, 110, 58, -116, 104, -15, -10, -41, -55, -30, -87, -21, -21, -37, -117, 102, -128, -6, 33, 66, -20, -3, 71, -118, -45, 55, -116, 104, 126, 110, -55, 6, -97, -86, -106, -26, -26, 13, 0, 113, -18, -114, -71, 80, 39, -44, 74, 64, -88, -127, -56, -50, -16, 9, 14, 36, 109, 64, 28, 66, -32, -71, 31, 42, 89, -42, -111, -86, 65, 127, -15, 78,
				-62, -18, -61, -45, -83, 59, -43, -55, -90, 22, -26, -21, -22, -125, 8, -79, 11, 26, -20, -46, 53, -116, 104, 126, -63, -38, -12, 10, 107, 105, 97, 94, 7, 16, 103, -31, -38, 14, -83, 104, 20, -106, 50, 13, 45, -81, -40, 6, 19, -44, -54, -78, 70, 23, -128, -123, 107, 101, 73, -61, -4, 66, 45, -87, -82, 106, -11, -82, 109, 38, 81, -35, 93, 85, -18, 4, -101, 82, 31, 45, 46, -103,
				95, -108, 20, -101, 78, 31, 31, 45, 70, 108, -71, -85, -87, -119, 36, -87, 78, 39, 31, 13, 70, 30, 72, -120, 77, 25, -47, 53, -89, -118, -1, -36, -77, -51, 65, -92, 123, 10, 126, -44, -51,
				96, -24, -124, 9, -95, 19, -94, 116, -7, 51, 79, 120, -49, 46, -62, -124, -40, -124, -88, 11, -41, -26, -38, -70, -19, -2, 99, -76, -66, -122, -21, 77, 23, -108, 14, -15, 74, -32, -72, -124,
				-24, -101, 13, -37, -99, -57, 105, -29, 27, 84, 119, -40, 116, -88, -79, 33, -95, -105, 16, -83, 110, -38, -18, 57, 94, -101, 24, -53, 6, -45, 33, -67, 58, -20, 114, 106, 3, 24, 95, 119, -27, 118, 4, -43, 96, -75, -62, 125, 54, 12, 40, 44, -87, 29, -63, -63, -98, 23, -52, -60, 119, 34, -35, -49, -48, 4, 59, -42, -26, 125, 97, -89, 122, -31, 80, -90, 90, 33, 74, 119, 6, 36, -110, 48, 59, 2, -85, 67, -4, 112, 47, 8, 102, 89, -119, 96, 60, -119, -94, 26, -80, 105, 95, 23, -69, 117, 15, 6, -77, -83, -116, 57, 116, 84, 119, -73, -96, -98, -79, -95, 44, 112, -72, 25, 27, -52, -96, -75, 107, 6, 29, 58, -86, 117, -96, 107, -20, -70, 53, -8, 36, 6, 50, -104, 109, 109, -37, -18, 30, -123, -74, -7, -61, 72, 20, -54, -80, 43, -77, 28, 12, -96, -70, 120, -61, 72
				, -80, 42, -85, -118, -35, -77, 100, 75, -43, 89, -53, 17, 69, -30, -28, -113, 108, -111, -6, -100, -8, -34, -101, -85, -47, 22, 30, 100, 56, -124, -79, -38, 120, 16, -19, 31, 123, 43, 84, -
				24, 30, 40, -42, -86, 47, -122, 63, -65, -106, -39, 28, 8, -46, -67, 72, -118, -51, 62, -17, 28, 126, 100, -114, -67, -5, 38, -108, 85, 103, 105, -107, -118, 85, 120, 53, -53, 102, -44, -123
				, 16, 32, 78, 86, 29, -36, 79, -55, -115, 69, -121, 21, 52, 108, -10, 30, -97, 11, 97, 66, 108, 86, 125, -100, 51, -82, -27, -56, -94, -61, -70, 123, 60, -45, -40, 124, 49, 114, 82, 23, 91,
				34, 106, 24, -47, 106, -82, 44, 58, -84, -115, 85, -40, -122, -94, 119, 98, -78, -7, 82, 111, -82, 103, -40, -111, -42, 33, 27, -14, 34, -128, 56, 57, -29, 64, 62, 99, 88, 84, -11, -104, -58
				, -38, 80, 103, -108, 16, 107, 68, -43, -116, 104, 43, -121, 49, 44, -86, -51, 45, 110, 54, -60, 28, 24, -56, 110, 77, -9, -28, 45, 75, -124, 84, -51, 30, -39, -5, 12, -39, -89, -29, -39, -116, -88, 43, -25, 22, 116, -84, 117, 118, 35, -88, -64, 0, 98, 31, 27, 89, -4, -42, -10, -123, 39, -89, 111, 23, -93, -67, -25, 60, -68, 19, 93, -102, -51, 63, -76, 125, -39, 73, -22, -31, 124, -92, -5, -100, 7, 83, -38, -62, 127, -15, 40, 37, 119, 75, 37, -91, -91, 71, -31, -2, -13, -114, 122, 9, 45, 60, 110, -97, 48, 64, 21, 68, -73, -125, -3, -65, -64, 1, 20, -102, 102, 119,
				108, 95, -81, 13, -35, 17, 79, -78, -48, 52, -101, -77, 125, -79, 118, 52, 39, -98, 100, -57, -45, 108, -15, -124, 2, -86, 32, 90, 20, 77, -78, -58, 52, 91, 60, 81, 65, 44, -84, -121, -117,
				-126, 73, 118, 52, -51, 78, 50, -96, 6, 34, -24, -48, -78, -45, 39, 46, 13, -30, 32, -102, -9, 4, -49, 122, 55, -97, -68, 52, -120, 85, 53, 49, -126, 79, -104, -20, 112, -128, -86, -120, 58
				, 64, 64, 77, -33, 57, 64, 21, 45, 125, 7, 2, 26, 76, -20, -95, -62, 116, 107, 122, -48, 1, -110, 8, 64, -28, 0, 29, -117, -117, -56, 1, 10, -118, -125, -56, 1, 10, -117, 65, 52, 25, -45, -79, 10, -39, -43, 108, -28, 56, -97, 33, -37, 23, -108, 62, 13, 57, 66, 18, 57, 66, 50, 57, 66, 50, 57, 66, 50, 57, 66, 50, 57, 66, 50, 57, 66, 50, 57, 66, 50, 57, 66, 50, 57, 66, 50, 57, 66,
				50, 57, 66, 50, 57, 66, 50, 57, 66, 50, 57, 66, 50, 57, 66, 50, 57, 66, 50, 57, 66, 50, 57, 66, 50, 57, 66, 50, 57, 66, 50, 37, 68, -24, -15, -50, -50, -109, 24, 123, -79, -77, -77, -13, 52
				, -82, -74, 19, 32, -12, -8, -39, -18, 94, 77, -49, 95, 60, -114, -125, -50, -53, -3, 90, -21, 7, -81, 94, -57, 66, 41, 118, 66, 79, 95, -18, 5, -76, 75, 61, -110, 118, -10, 3, -83, 31, -68,
				-55, 32, -95, -17, 15, -10, -62, 34, -19, -60, -45, 87, -111, -42, -9, -23, -25, 114, -52, -124, 94, -17, 49, 122, 73, 8, -24, 57, -45, -6, -63, -21, 108, 17, -30, 0, 34, 68, -60, 1, 84, -47, 78, -106, 8, -19, -20, 113, 69, 53, -47, 118, -71, -83, 31, 16, -121, -125, 56, 9, 61, -35, -25, 19, -38, -93, 49, -117, 103, 64, -21, -69, -39, 33, -12, 102, 47, -50, 62, 60, 61, -128, -
				102, -89, -99, 103, 113, 18, 2, -69, 64, 50, -120, 94, -125, -83, -45, 14, -94, 24, 9, 125, 15, 118, 97, -17, 5, 65, -13, -49, -31, -26, 73, -99, 40, 70, 66, 47, -31, 46, -20, -101, -73, -2,
				20, 110, 125, -113, 52, -30, -57, 72, 104, 87, -48, 7, -13, -42, 119, 4, -83, 83, 12, -47, 36, 8, 9, -70, 64, 96, -90, -49, 4, -83, -109, 26, 81, 102, 9, -67, 113, -124, 28, 33, 67, -67, -74, 68, -24, 7, -62, -93, 22, 115, -31, -44, -37, 91, 63, 68, 8, -7, -2, 58, -43, 49, 40, 47, -32, 46, 60, 39, 104, 94, 64, -120, 44, -38, 111, -84, -5, 62, 75, -120, -20, -64, -59, -104, 51,
				-58, 87, 112, -13, 84, -75, -58, -38, 113, -115, 28, 66, -66, 127, -97, 102, 24, -19, -125, 93, -96, 72, 122, 97, 35, 122, 69, -61, 103, -93, 126, -114, 53, -105, -112, -65, -10, -106, -30,
				43, -64, -108, -123, -90, 66, 4, -2, 7, -48, -84, 92, -33, 30, 30, 31, -57, 39, -28, -5, -85, 20, -114, 13, 44, -99, -120, 42, 56, -48, 44, 38, 25, 66, -37, -115, -77, -29, 32, 66, 36, 71,
				-61, 61, -119, -41, 72, -7, 11, -65, 125, 10, 23, 10, 28, -47, 8, 19, -94, 112, -20, 120, -85, -80, -36, 49, 122, 64, 80, 89, 9, 29, -88, 43, 32, 68, 113, -2, 89, -52, -107, -4, -35, 56, 0,
				69, 78, -45, 21, 18, -14, -41, -116, 95, 7, -12, 36, -30, -89, -60, 123, 17, -47, -75, -57, -82, -7, 20, 91, -119, 28, -91, 43, 38, 68, 113, 86, -18, -101, 96, -87, -15, 37, -11, -74, -24, -
				29, 96, 90, -76, 111, -114, -97, 125, 43, -116, -116, 16, -123, 99, 127, -1, -78, 102, 24, -5, -15, 108, 27, 63, 126, -74, 91, -5, 63, -40, 125, 65, -32, 64, -100, 83, -49, -27, -124, 114, 117, -88, -71, 88, -36, 87, -26, 32, 8, 101, -5, -43, 28, 120, 1, 47, -15, 64, 17, 58, 1, -57, -62, -78, -121, -99, -85, 17, -54, -25, -23, -26, 65, -63, -17, -19, -62, 18, -54, -28, 107, -90
				, -16, 18, -68, -112, 10, 77, 40, -5, 47, -62, -127, 5, -65, 34, 71, -115, 80, 94, 29, 91, -14, -14, 64, 37, 66, 25, 126, 33, 23, 44, -39, -117, 76, -43, 8, 101, -11, -91, 110, -80, 36, -81,
				123, -45, 32, 68, 84, 92, 75, -117, -34, -54, -33, 96, -86, 76, 40, 47, 111, -98, -86, 106, 123, 11, -47, 93, 13, 66, 121, 113, 108, -28, -21, 93, -75, 8, -27, -62, -79, -79, -81, -102, -42
				, 35, 68, -74, 29, 98, 77, 27, 82, -121, 54, 37, -108, 113, -57, 70, 56, -76, 49, 33, -94, -19, 16, 43, -38, 86, 121, -33, -67, 1, 33, -62, 13, -20, 68, -75, -79, -82, -44, 73, 35, 66, -103,
				44, -82, 33, -34, 44, 77, 72, 40, 123, -114, -115, 119, 104, 42, 66, 4, -37, 33, 73, 106, 5, -17, -48, 100, -124, -78, 84, 92, 67, -67, -34, 62, 6, 66, -103, 121, -73, -103, -102, 67, -109,
				18, -54, -124, 99, -85, 58, 52, 45, -95, -12, -65, -64, -117, 125, 113, 107, -46, -124, 82, -18, -40, 26, 14, 77, 78, 40, -51, -114, -83, -27, -48, 49, 16, 74, -19, 118, -56, -70, -2, 0, 34
				, 38, -108, -50, -19, -112, -69, -104, 50, 89, 98, -124, -46, 87, 92, 67, -106, -55, 18, 36, -108, -78, 13, -20, 123, 70, 19, 44, 30, 66, 105, 114, 108, 35, -121, -114, -111, 80, 106, -118,
				107, 10, 101, -78, -124, 9, -91, 99, 59, 4, -75, -111, 97, -115, -112, 125, -57, 54, 119, 104, -128, -48, -113, 84, -19, 90, -34, 14, -63, 110, 100, 32, -12, 99, -104, 80, -45, -69, -9, 31,
				-88, -102, -74, -73, -127, 45, -33, 106, -58, -22, -61, -5, 119, 77, -116, 126, -6, -103, -118, -111, 45, -57, -90, 113, -24, 42, -97, -97, 127, 106, -30, -22, -89, 95, 126, 37, -6, 10, 27,
				-114, 77, -27, -48, -2, -81, -65, 0, 124, 106, -6, 72, -59, 40, -23, -19, 16, -59, -115, 12, 1, -97, -113, 77, 18, 125, -4, -115, -26, -101, -110, 45, -82, -23, -106, -55, -94, -6, 77, -54,
				-89, 22, -7, -119, 24, 37, -73, 29, -94, -66, -111, 1, -16, -7, 1, -61, -89, 22, -5, -33, -109, 124, 97, 82, -114, 77, -28, -48, -17, 127, -60, -14, -87, 7, 127, -110, 47, 77, 98, 3, 91, 105
				, -85, 89, -64, -25, -99, 10, -97, 26, 35, -102, -32, 31, 123, 113, -115, -60, -95, 63, -4, -84, -52, -89, 42, -102, 4, 41, 94, -57, 38, 113, 104, 48, -3, 65, -120, 36, -8, 43, 108, -121, 124, -6, -70, 42, -12, -97, -21, 111, 100, 4, 36, 15, -17, -15, 51, -62, 108, -121, -36, -102, -5, 124, -77, -36, -48, -51, 47, 115, -73, -28, -97, 49, -40, -56, 32, -29, 83, 21, 69, -16, -105
				, 20, -41, -106, 22, 30, -107, 25, 61, 90, 16, 67, -94, 40, -109, -31, -61, -69, -124, -47, -17, -26, -41, 34, 112, -20, 91, 95, -54, -128, -2, 37, -104, 112, 4, 14, -3, 59, 17, -97, -86, 8,
				-126, 63, -28, -40, 48, -97, -38, 116, -5, -60, -1, 20, -127, 67, -85, -121, 119, 25, 35, -29, -64, -58, 45, -82, -3, -69, 44, -47, 103, -50, -69, -82, -51, -53, 100, 31, -88, -7, 84, 101,
				30, -4, -39, -19, -112, 79, -113, 100, -128, -54, -27, -1, 48, -61, -56, 120, 35, -61, 36, -68, -117, 25, 25, 87, 71, 34, -114, 61, -9, 95, 57, -96, -118, 22, 104, 29, 90, 92, -35, 48, -107,
				105, -16, 15, 109, 96, 127, 70, -15, -87, -24, 75, -48, -95, 13, 7, 16, 69, 120, -105, 48, 50, 12, -2, -57, 27, -40, 95, -80, -128, 42, 65, -19, -56, -116, 76, -73, -102, 113, -43, 13, 83,
				-103, 38, 72, -121, -114, -83, 0, -88, -110, 28, 45, 81, 56, 52, 85, -6, 35, -105, 97, 117, -92, -74, 29, -94, 4, -88, 50, -118, 38, -116, 55, 50, -44, -86, 27, -90, 50, 76, -112, -18, 111,
				74, -93, 60, -29, 69, -122, 27, 25, 113, -124, 119, 9, 35, -93, -32, -1, 63, 85, 64, -27, 114, -97, -63, -41, 105, 86, 55, 76, 101, -112, 32, -11, -9, -88, 19, 42, -9, -21, -13, -119, 51, -68, -117, -91, 27, -4, 75, 26, -128, -54, 127, 116, 107, 125, 87, -4, -31, 61, 6, 70, 127, -22, 0, 42, -105, -81, 100, -112, 79, 85, -22, -63, -65, 87, 15, 80, -71, 92, 84, -3, -90, -28, -62,
				-69, -124, -111, 98, 117, -28, -86, 46, -95, -65, -44, -66, -121, -78, -70, 97, 42, -75, -32, 63, -96, 75, 72, 41, -100, 37, 31, -34, 101, -116, 20, 2, -101, 86, 40, 43, -9, 116, -31, -65,
				33, -106, -22, -122, -87, 84, -126, 127, -81, 70, 48, -5, 3, 31, -19, 109, -122, 119, 49, 35, 124, 117, -92, -5, 47, 85, 64, 37, 116, -84, -113, -73, -70, 97, 42, 124, -16, -1, 91, 13, -48,
				-33, 88, 64, 105, 8, -17, 18, 70, -40, -32, -81, -28, -41, 127, 34, 27, 77, -90, -70, 97, 42, 108, -126, 84, 68, -5, 117, -49, 0, -110, 79, -118, -62, -69, 88, -56, -22, 72, -1, 31, 72, 64,
				56, -113, 78, -74, -70, 97, 42, 92, -126, -44, -115, 10, 105, -91, 94, 20, -97, 119, -74, -5, -84, -52, 8, 21, -4, 17, 126, 125, 21, -31, -47, -106, -86, 27, -90, 66, 37, 72, 125, -46, 32, -
				122, -31, -109, -26, -16, 46, -42, 63, -14, -70, -23, -128, -40, -81, -27, 30, -67, -4, -113, -19, 94, 106, 107, 102, 100, 124, 98, 98, 69, -54, 72, -76, 4, -23, -111, -82, -26, -105, 87, 38
				, 38, -58, 71, 102, 108, -9, 85, 71, 67, -41, -114, 54, 112, 100, 59, -128, -80, 95, -105, 100, 65, 108, -11, 104, 107, -23, -38, -112, -7, 21, 39, -86, -55, -31, -15, -32, 109, 8, -110, 26,
				124, 55, 80, 14, -111, 45, 52, -18, 7, 111, -106, 24, 31, -98, -76, -35, 107, -68, 102, 70, -90, -93, -9, -6, 72, 110, 22, -69, -94, -31, -47, 15, -94, -9, 36, 77, 103, 101, -78, -51, -116,
				-14, 110, 103, -39, 124, 32, -36, 77, -26, 44, 65, -124, -59, -96, -75, 7, -36, 123, -74, 70, 51, -64, -120, -49, -89, -74, 97, 42, -36, 113, -113, -6, -75, -80, 24, -76, 6, 63, 19, -111, 118, 70, 48, -97, 26, -93, -73, -126, -64, -42, 31, -14, 107, -47, 66, 99, -7, -83, -16, -42, -47, 52, 51, 18, -13, -87, 73, 16, -4, -125, 37, 35, -127, 71, 47, -53, 111, 27, 77, 43, -93, 73,
				-58, -97, -7, -116, -32, -32, -33, 88, -126, -64, -59, -96, 85, -44, 41, 35, -45, 35, 105, -116, 107, -61, 40, 62, 85, -63, 9, 82, -79, 54, -116, 74, 96, 30, -67, -118, -66, 123, 125, 122,
				-40, 54, -113, -88, -122, 102, -79, -41, 94, -43, 54, 24, -4, -69, -117, 69, 112, -4, 60, 80, 122, 72, 100, 54, 85, 57, -28, -92, -36, -128, 34, -110, 37, 72, 28, 62, -54, 79, -49, -114, -90
				, 103, -86, 93, 71, 79, -80, 32, 35, -107, -37, -19, -42, -42, 117, -98, 46, -98, -66, 110, -101, 76, 93, -125, 99, 26, 23, 95, -43, 6, -106, -47, -102, -10, 35, -95, 99, -125, -74, -23, 84,
				52, -94, 121, -15, 53, -55, 87, -2, -88, -16, 46, -48, -120, 109, 62, -125, 74, 14, -83, -63, -56, -116, 79, 69, -77, 118, -121, -111, -106, 3, 69, 36, -86, -114, -32, -61, 59, 44, -101, 110, 52, -87, -21, 64, 81, 70, 64, 117, -28, 62, -47, -61, 123, 99, -74, -126, -38, 13, -126, 1, 116, 40, 94, -16, 87, 15, -17, -96, -90, 111, 88, 1, 52, 69, -42, -127, 58, -93, 80, 96, 91, 35
				, -28, 83, -43, 84, -14, 124, 38, 77, 45, -102, 81, 32, -8, -21, -121, 119, 80, -77, 73, -49, -76, 33, -70, 25, 22, 96, 84, -81, -114, 72, -86, 27, -102, -102, 78, 118, 21, 50, 28, 67, 23, 106, 90, 89, 54, 14, -17, -96, -110, 92, -52, 42, 47, -61, -46, -95, 81, -83, -50, -2, 31, -15, -126, 87, -92, 112, -54, -112, 36, 0, 0, 0, 37, 116, 69, 88, 116, 100, 97, 116, 101, 58, 99, 114, 101, 97, 116, 101, 0, 50, 48, 50, 51, 45, 48, 54, 45, 51, 48, 84, 49, 52, 58, 53, 50, 58, 49, 55, 43, 48, 48, 58, 48, 48, -38, -69, 82, -86, 0, 0, 0, 37, 116, 69, 88, 116, 100, 97, 116, 101, 58, 109, 111, 100, 105, 102, 121, 0, 50, 48, 50, 51, 45, 48, 54, 45, 51, 48, 84, 49, 52, 58, 53, 50, 58, 49, 55, 43, 48, 48, 58, 48, 48, -85, -26, -22, 22, 0, 0, 0, 40, 116, 69, 88, 116,
				100, 97, 116, 101, 58, 116, 105, 109, 101, 115, 116, 97, 109, 112, 0, 50, 48, 50, 51, 45, 48, 54, 45, 51, 48, 84, 49, 52, 58, 53, 50, 58, 53, 52, 43, 48, 48, 58, 48, 48, 73, 81, -33, -82, 0, 0, 0, 0, 73, 69, 78, 68, -82, 66, 96, -126};
	}
}
