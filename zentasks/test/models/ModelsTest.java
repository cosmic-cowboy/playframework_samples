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
		String email     = "bob@gmail.com";
		String name      = "bob";
		String password  = "secret";
		
		// テストデータの登録
		new User(email, name, password).save();
		// 登録したテストデータの取得		
		User bob = User.authenticate(email, password);
		// 確認：登録した値があること
		assertThat(bob).isNotNull();
		// 確認：登録した値が正しいこと
        assertThat(bob.name).isEqualTo(name);
        
		// 確認：登録されていないユーザ
        assertThat(User.authenticate(email, "badpassword")).isNull();
        assertThat(User.authenticate("tom@gmail.com", password)).isNull();
        
	}
}
