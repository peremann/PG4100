package no.westerdals.pg4100.mockitolecture.assignment3;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;

public class ServletTest {

	// Annotations may be used...
	private HttpServletRequest request;
	private HttpServletResponse response;
	private RequestDispatcher view;
	private ServletHelper helper;
	
	@Before
	public void init(){
		request = mock( HttpServletRequest.class );
		response = mock( HttpServletResponse.class );
		view = mock(RequestDispatcher.class);
		helper = mock(ServletHelper.class);
	}
	
	@Test
	/**
	 * Mocking.
	 * Test:
	 * <ul>
	 * <li> the value of the request parameter "input" is used when calling model (helper)
	 * </li>
	 * <li> the servlet forwards the request
	 * </li> 
	 * </ul>
	 * @throws Exception
	 */
	public void servletUsesModelAndForwardsRequest() throws Exception{
		// ARRANGE
		when(request.getParameter("input")).thenReturn("inputValue");
		when(request.getRequestDispatcher(anyString())).thenReturn(view);
		ExampleServlet exampleServlet = new ExampleServlet();
		exampleServlet.setHelper(helper);
		// ACT
		exampleServlet.doPost( request, response );
		//ASSERT / VERIFY
		verify(helper).provideAnswer("inputValue");
		verify(view).forward(request, response);
	}

}
