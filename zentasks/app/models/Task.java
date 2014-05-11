package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;

@Entity
public class Task extends Model{

	private static final long serialVersionUID = 1L;

	@Id
	public Long id;	
	public String title;
	public boolean done;
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
	
}
