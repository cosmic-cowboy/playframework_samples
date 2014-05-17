package controllers;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.*;

import java.util.List;

import models.Project;

import org.junit.Before;
import org.junit.Test;

import play.libs.Yaml;
import play.mvc.Result;
import play.test.WithApplication;

import com.avaje.ebean.Ebean;
import com.google.common.collect.ImmutableMap;

public class ProjectsTest extends WithApplication {

	@Before
	public void setUp() {
		start(fakeApplication(inMemoryDatabase(), fakeGlobal()));
		Ebean.save((List) Yaml.load("test-data.yml"));
	}
	
	@Test
	public void newProject() {
		Result result = callAction(
				controllers.routes.ref.Projects.add(),
				fakeRequest()
				.withSession("email", "bob@example.com")
				.withFormUrlEncodedBody(ImmutableMap.of(
		            "group", "someGroup"))
		);
        assertThat(status(result)).isEqualTo(200);
        Project project = 
        		Project.find.where().eq("folder", "someGroup").findUnique();
        assertThat(project).isNotNull();
        assertThat(project.name).isEqualTo("NewProject");
        assertThat(project.members.size()).isEqualTo(1);
        assertThat(project.members.get(0).email).isEqualTo("bob@example.com");
        
	}
	
	@Test
	public void renameProject(){
		long projectId = Project.find.where()
				.eq("members.email", "bob@example.com")
				.eq("name", "Private").findUnique().id;
				
		Result result = callAction(
				controllers.routes.ref.Projects.rename(projectId),
				fakeRequest()
				.withSession("email", "bob@example.com")
				.withFormUrlEncodedBody(ImmutableMap.of(
		            "name", "NewName"))
		);
        assertThat(status(result)).isEqualTo(200);
        Project project = 
        		Project.find.byId(projectId);
        assertThat(project.name).isEqualTo("NewName");
	}
	
	@Test
	public void renameProjectForbidden(){
		long projectId = Project.find.where()
				.eq("members.email", "bob@example.com")
				.eq("name", "Private").findUnique().id;
				
		Result result = callAction(
				controllers.routes.ref.Projects.rename(projectId),
				fakeRequest()
				.withSession("email", "jeff@example.com")
				.withFormUrlEncodedBody(ImmutableMap.of(
		            "name", "NewName"))
		);
        assertThat(status(result)).isEqualTo(403);
        Project project = 
        		Project.find.byId(projectId);
        assertThat(project.name).isEqualTo("Private");
	}
}
