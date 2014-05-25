package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.avaje.ebean.Ebean;

import play.data.validation.Constraints.*;
import play.data.format.*;
import play.db.ebean.Model;
import play.mvc.Content;

@Entity
public class Task extends Model{

	private static final long serialVersionUID = 1L;

	@Id
	public Long id;
	@Required
	public String title;
	public boolean done;
	@Formats.DateTime(pattern="MM/dd/yy")
	public Date dueDate;
	@ManyToOne
	public User assignedTo;
	public String folder;
	@ManyToOne
	public Project project;
	
	public static Finder<Long, Task> find = 
			new Finder<Long, Task>(Long.class, Task.class);
	
	/**
	 * @param ownerEmail
	 * @return
	 */
	public static List<Task> findTodoInvolving(String ownerEmail){
		return find.fetch("project").where()
				.eq("done", false)
				.eq("project.members.email", ownerEmail)
				.findList();
	}
	
	/**
	 * タスク作成
	 * @param task
	 * @param projectId
	 * @param folder
	 * @return
	 */
	public static Task create(Task task, Long projectId, String folder){
		task.project = Project.find.ref(projectId);
		task.folder = folder;
		task.save();
		return task;
		
	}
	
	/**
	 * プロジェクトごとのタスクを取得
	 * @param projectId
	 * @return
	 */
	public static List<Task> findByProject(Long projectId){
		return find.where().eq("project.id", projectId).findList();
	}

	/**
	 * タスクの担当者かどうかを判定
	 * @param taskId
	 * @param ownerEmail
	 * @return
	 */
	public static boolean isOwnerOf(Long taskId, String ownerEmail) {
		return find.where()
				.eq("id", taskId)
				.eq("assignedTo.email", ownerEmail)
				.findRowCount() > 0;
	}

	
	/**
	 * タスクの進捗状況を更新する
	 * @param taskId
	 * @param progress
	 */
	public static void markAsDone(Long taskId, Boolean progress) {
		Task task = Task.find.ref(taskId);
		task.done = progress;
		task.update();
	}

	/**
	 * フォルダー名を変更する
	 * @param projectId
	 * @param folder
	 * @param newName
	 * @return
	 */
	public static String renameFolder(Long projectId, String folder,
			String newName) {
		Ebean.createSqlUpdate(
			"update task set folder = :newName where folder = :folder and project_id = :projectId"
		)
		.setParameter("newName", newName)
		.setParameter("folder", folder)
		.setParameter("projectId", projectId)
		.execute();
		return newName;
	}

	/**
	 * フォルダを削除・フォルダ内のタスクも削除
	 * @param projectId
	 * @param folder
	 */
	public static void deleteInFolder(Long projectId, String folder) {
		Ebean.createSqlUpdate(
			"delete from task where project_id = :projectId and folder = :folder"
		)
		.setParameter("projectId", projectId)
		.setParameter("folder", folder)
		.execute();
		
	}
}
