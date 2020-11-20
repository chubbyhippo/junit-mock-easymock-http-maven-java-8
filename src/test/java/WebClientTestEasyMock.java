import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.InputStream;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class WebClientTestEasyMock {
	
	private ConnectionFactory factory;
	private InputStream stream;

	@BeforeEach
	public void setUp() {
		factory = createMock("factory", ConnectionFactory.class);
		stream = createMock("stream", InputStream.class);
	}

	@Test
	public void testGetContentOk() throws Exception {
		expect(factory.getData()).andReturn(stream);
		expect(stream.read()).andReturn((int)'W');
		expect(stream.read()).andReturn((int)'o');
		expect(stream.read()).andReturn((int)'r');
		expect(stream.read()).andReturn((int)'k');
		expect(stream.read()).andReturn((int)'s');
		expect(stream.read()).andReturn((int)'!');
		expect(stream.read()).andReturn(-1);
		stream.close();
		
		replay(factory);
		replay(stream);
		
		WebClient client = new WebClient();
		String workingContent = client.getContent(factory);
		
		assertEquals("Works!", workingContent);
	}
	
	@Test
	public void testGetContentCannotCloseInputStream() throws Exception {
		expect(factory.getData()).andReturn(stream);
		expect(stream.read()).andReturn(-1);
		stream.close();
		expectLastCall().andThrow(new IOException("cannot close"));
		
		replay(factory);
		replay(stream);
		WebClient client = new WebClient();
		String workingContent = client.getContent(factory);
		
		assertNull(workingContent);
		
	}
	
	@AfterEach
	public void tearDown() {
		verify(factory);
		verify(stream);
	}

}
