package controllers;

import java.util.ArrayList;

import models.Project;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.projects.*;
import static play.data.Form.*;

/**
 * クラスに対してセキュリティ認証のアノテーションを追加することで、
 * このクラスで宣言されるすべてのメソッドが認証処理を通ることになる。
 *
 */
@Security.Authenticated(Secured.class)
public class Projects extends Controller {

	/**
	 * プロジェクトを追加する
	 * 
	 * @return
	 */
	public static Result add(){
		String folder = form().bindFromRequest().get("group");
		String ownerEmail = request().username();
		Project project = Project.create("NewProject", folder, ownerEmail);
		return ok(item.render(project));
	}

	/**
	 * プロジェクトをリネームする
	 * 
	 * @param projectId
	 * @return
	 */
	public static Result rename(Long projectId){
		if(Secured.isMemberOf(projectId)){
			String newName = form().bindFromRequest().get("name");
			String renameProject = Project.rename(projectId, newName);
			return ok(renameProject);
		} else {
			return forbidden();			
		}
	}
	
	/**
	 * プロジェクトを削除する
	 * @param projectId
	 * @return
	 */
	public static Result delete(Long projectId){
		if(Secured.isMemberOf(projectId)){
			Project deleteProjcet = Project.find.ref(projectId);
			deleteProjcet.delete();
			return ok();
		} else {
			return forbidden();			
		}
	}

	/**
	 * グループを追加する
	 * 
	 * @return
	 */
	public static Result addGroup(){
		return ok(
			group.render("NewGroup", new ArrayList<Project>())
		);
	}
	
	/**
	 * グループをリネームする
	 * @param group
	 * @return
	 */
	public static Result renameGroup(String group){
		String newName = form().bindFromRequest().get("name");
		return ok(Project.renameFolder(group, newName));

	}
	
	/**
	 * グループを削除する
	 * @param group
	 * @return
	 */
	public static Result deleteGroup(String group){
		Project.deleteInFolder(group);
		return ok();
	}
	
	/**
	 * プロジェクトにメンバーを追加する
	 * @param projectId
	 * @return
	 */
	public static Result addUser(Long projectId){
		if(Secured.isMemberOf(projectId)){
			String username = form().bindFromRequest().get("user");
			Project.addMember(projectId, username);
			return ok();
		} else {
			return forbidden();
		}
	}

	/**
	 * プロジェクトからメンバーを削除する
	 * @param projectId
	 * @return
	 */
	public static Result removeUser(Long projectId){
		if(Secured.isMemberOf(projectId)){
			String username = form().bindFromRequest().get("user");
			Project.removeMember(projectId, username);
			return ok();
		} else {
			return forbidden();
		}
	}
}
