package controllers;

import java.util.List;

import models.Project;
import models.Task;
import play.data.Form;
import play.mvc.*;
import views.html.helper.form;
import views.html.tasks.*;
import static play.data.Form.*;

/**
 * タスク関連アクション
 *
 */
@Security.Authenticated(Secured.class)
public class Tasks extends Controller{

	/**
	 * プロジェクト内のタスクを表示
	 * @param projectId
	 * @return
	 */
	public static Result index(Long projectId){
		if(Secured.isMemberOf(projectId)){
			Project project     = Project.find.byId(projectId);
			List<Task> taskList = Task.findByProject(projectId);
			return ok(index.render(project, taskList));
		} else {
			return forbidden();
		}
	}
	
	public static Result add(Long projectId, String folder){
		if(Secured.isMemberOf(projectId)){
			Form<Task> taskForm = form(Task.class).bindFromRequest();
			if(taskForm.hasErrors()){
				return badRequest();
			} else {
				Task task = Task.create(taskForm.get(), projectId, folder);
				return ok(item.render(task));
			}
		} else {
			return forbidden();
		}
	}
}
