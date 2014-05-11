package models;

import org.junit.Before;
import org.junit.Test;

import play.test.*;
import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.*;

public class ModelsTest extends WithApplication{
	
	@Before
	public void setUp(){
		start(fakeApplication(inMemoryDatabase()));
	}
	
	@Test
	public void createAndRetrieveUser(){
		String email = "bob@gmail.com";
		String name  = "bob";
		
		// テストデータの登録
		new User(email, name, "secret").save();
		// 登録したテストデータの取得		
		User bob = User.find.where().eq("email", email).findUnique();

		// 確認：登録した値があること
		assertThat(bob).isNotNull();
		// 確認：登録した値が正しいこと
        assertThat(bob.name).isEqualTo(name);
	}
}
