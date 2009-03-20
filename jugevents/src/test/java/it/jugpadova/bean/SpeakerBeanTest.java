package it.jugpadova.bean;

import it.jugpadova.po.Speaker;
import junit.framework.TestCase;

public class SpeakerBeanTest extends TestCase {

	public void testSpeakerBean() {
		try {
			SpeakerBean sb = new SpeakerBean(null);
			fail("Exception should be thrown");
		} catch (Exception e) {
			assertTrue(e instanceof IllegalArgumentException);
		}
	}

	public void testEqualsObject() {
		Speaker mario = new Speaker();
		mario.setFirstName("Mario");
		mario.setLastName("Rossi");
		mario.setEmail("aaa@bbb.com");
		
		Speaker Mario = new Speaker();
		Mario.setFirstName("MARIO");
		Mario.setLastName("rossi");
		Mario.setEmail("xxx@yyy.com");
		
		Speaker Antonio = new Speaker();
		Antonio.setFirstName("Antonio");
		Antonio.setLastName("rossi");
		Antonio.setEmail("xxx@yyy.com");
		
		SpeakerBean marioBean = new SpeakerBean(mario);
		SpeakerBean MarioBean = new SpeakerBean(Mario);
		SpeakerBean AntonioBean = new SpeakerBean(Antonio);
		assertEquals(marioBean, MarioBean);
		assertFalse(marioBean.equals(AntonioBean));
		
	}

}
