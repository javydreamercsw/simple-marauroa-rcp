package simple.common;

import simple.common.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

/**
 * Test CommandlineParser.
 * 
 * @author Martin Fuchs
 */
public class CommandlineParserTest {

	@Test
	public final void testGetNextParameter() {
		ErrorBuffer errors = new ErrorBuffer();
		CommandlineParser parser = new CommandlineParser("who");
		assertEquals("who", parser.getNextParameter(errors));
		assertEquals(null, parser.getNextParameter(errors));
		assertEquals(false, errors.hasError());

		errors = new ErrorBuffer();
		parser = new CommandlineParser("where ghost");
		assertEquals("where", parser.getNextParameter(errors));
		assertEquals("ghost", parser.getNextParameter(errors));
		assertEquals(null, parser.getNextParameter(errors));
		assertEquals(false, errors.hasError());
	}

	@Test
	public final void testReadAllParameters() {
		ErrorBuffer errors = new ErrorBuffer();
		CommandlineParser parser = new CommandlineParser("jail player minutes reason");
		List<String> paras = parser.readAllParameters(errors);
		assertEquals("[jail, player, minutes, reason]", paras.toString());
		assertEquals(false, errors.hasError());

		errors = new ErrorBuffer();
		parser = new CommandlineParser("/hello, how are you?");
		paras = parser.readAllParameters(errors);
		assertEquals("[/hello,, how, are, you?]", paras.toString());
		assertEquals(false, errors.hasError());
	}

	@Test
	public final void testQuoting() {
		final ErrorBuffer errors = new ErrorBuffer();
		final CommandlineParser parser = new CommandlineParser("where 'player 2'");
		assertFalse(errors.hasError());
		assertEquals("where", parser.getNextParameter(errors));
		assertEquals("player 2", parser.getNextParameter(errors));
		assertEquals(null, parser.getNextParameter(errors));
	}

	@Test
	public final void testError() {
		final ErrorBuffer errors = new ErrorBuffer();
		final CommandlineParser parser = new CommandlineParser("TEST 'abc...");
		assertFalse(errors.hasError());
		assertEquals("TEST", parser.getNextParameter(errors));
		assertEquals("abc...", parser.getNextParameter(errors));
		assertTrue(errors.hasError());
		assertEquals(null, parser.getNextParameter(errors));
	}

}
