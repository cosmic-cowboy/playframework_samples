package models;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.avaje.ebean.Ebean;

import play.libs.Yaml;
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
	
	@Test
	public void findProjectsInvolving(){
		String bobEmail    = "bob@gmail.com"; 
		String bobName     = "Bob"; 
		String bobProject  = "BobProject"; 
		String janeEmail   = "jane@gmail.com"; 
		String janeName    = "jane"; 
		String janeProject = "janeProject"; 

		// テストユーザ作成
		new User(bobEmail,  bobName, "secret").save();
		new User(janeEmail, janeName, "secret").save();
		
		// テストプロジェクト作成
		Project.create(bobProject,  "project", bobEmail);
		Project.create(janeProject, "project", janeEmail);
		List<Project> list = Project.findInvolving(bobEmail);
		
		assertThat(list.size()).isEqualTo(1);
		assertThat(list.get(0).name).isEqualTo(bobProject);
		
	}
	
	@Test
	public void findTaskInvolving(){
		String bobEmail    = "bob@gmail.com"; 
		String bobName     = "Bob"; 
		String bobProjectName  = "BobProject";
		String bobTaskTitle = "Release next version";
		
		// テストユーザ作成
		User bob = new User(bobEmail,  bobName, "secret");
		bob.save();
		
		// テストプロジェクト作成
		Project bobProject = Project.create(bobProjectName,  "project", bobEmail);
		
		// テストタスク作成
		Task bobTask = new Task();
		bobTask.title = bobTaskTitle;
		bobTask.project = bobProject;
		bobTask.save();
		
		List<Task> list = Task.findTodoInvolving(bobEmail);
		
		assertThat(list.size()).isEqualTo(1);
		assertThat(list.get(0).title).isEqualTo(bobTaskTitle);
				
	}
	
	@Test
	public void fullTest(){
		Ebean.save((List)Yaml.load("test-data.yml"));
		

        // Count things
		assertThat(User.find.findRowCount()).isEqualTo(3);
		assertThat(Project.find.findRowCount()).isEqualTo(7);
		assertThat(Task.find.findRowCount()).isEqualTo(5);

        // Try to authenticate as users
		assertThat(User.authenticate("bob@example.com", "secret")).isNotNull();
		assertThat(User.authenticate("jane@example.com", "secret")).isNotNull();
		assertThat(User.authenticate("jeff@example.com", "badpassword")).isNull();
		assertThat(User.authenticate("tom@example.com", "secret")).isNull();

        // Find all Bob's projects
        List<Project> bobsProjects = Project.findInvolving("bob@example.com");
        assertThat(bobsProjects.size()).isEqualTo(5);

        // Find all Bob's todo tasks
        List<Task> bobsTasks = Task.findTodoInvolving("bob@example.com");
        assertThat(bobsTasks.size()).isEqualTo(4);
	}
}
