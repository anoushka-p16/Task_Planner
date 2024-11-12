package edu.ncsu.csc216.wolf_tracker.model.project;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import edu.ncsu.csc216.wolf_tracker.model.log.AllTasksLog;
import edu.ncsu.csc216.wolf_tracker.model.log.CategoryLog;
import edu.ncsu.csc216.wolf_tracker.model.task.Task;
import edu.ncsu.csc216.wolf_tracker.model.util.LogList;

/**
 * Tests the Project class.
 * 
 * @author Anoushka Piduru
 */
class ProjectTest {
	/** Instance of Project for testing. */
	private Project project;
	/** Instance of CategoryLog for testing. */
	private CategoryLog categoryLog;
	/** Instance of AllTasksLog for testing. */
	private AllTasksLog allTasksLog;
	/** Instance of CategoryLog list for testing. */
	private LogList<CategoryLog> categoryLogs;
	/** Instance of Task for testing. */
	private Task task;

	/**
	 * Tests the Project constructor.
	 */
	@Test
	void testProjectConstruction() {
		assertThrows(IllegalArgumentException.class, () -> new Project(null));
		assertThrows(IllegalArgumentException.class, () -> new Project(""));
		assertThrows(IllegalArgumentException.class, () -> new Project("All Tasks"));

		project = new Project("MA305");

		assertEquals("MA305", project.getProjectName());
		assertTrue(project.isChanged());
		assertEquals("All Tasks", project.getCurrentLog().getName());
	}

	/**
	 * Tests saving project to file.
	 */
	@Test
	void testSaveProject() {
		project = new Project("MA305");

		assertThrows(IllegalArgumentException.class, () -> project.saveProject(null));
		assertTrue(project.isChanged());
	}

	/**
	 * Tests saving summary statistics to file.
	 */
	@Test
	void testSaveStats() {
		project = new Project("MA305");
		assertNotNull(project);

		assertThrows(IllegalArgumentException.class, () -> project.saveStats(null));
	}

	/**
	 * Tests setting the project name.
	 */
	@Test
	void testSetProjectName() {
		project = new Project("MA305");

		assertThrows(IllegalArgumentException.class, () -> project.setProjectName(null));
		assertThrows(IllegalArgumentException.class, () -> project.setProjectName(""));
	}

	/**
	 * Tests the addCategoryLog method.
	 */
	@Test
	void testAddCategoryLog() {
		project = new Project("MA305");

		assertThrows(IllegalArgumentException.class, () -> project.addCategoryLog(null));
		assertThrows(IllegalArgumentException.class, () -> project.addCategoryLog(""));
		assertThrows(IllegalArgumentException.class, () -> project.addCategoryLog("All Tasks"));

		project.addCategoryLog("Homework");
		assertEquals("Homework", project.getCurrentLog().getName());

	}

	/**
	 * Tests the getCategoryNames method.
	 */
	@Test
	void testgetCategoryNames() {
		project = new Project("MA305");

		project.addCategoryLog("Homework");
		project.addCategoryLog("Notes");
		project.addCategoryLog("Labs");

		String[] categoryNames = project.getCategoryNames();
		assertEquals(4, categoryNames.length);
		assertEquals("All Tasks", categoryNames[0]);
		assertEquals("Homework", categoryNames[1]);
		assertEquals("Labs", categoryNames[2]);
		assertEquals("Notes", categoryNames[3]);

	}

	/**
	 * Tests the setCurrentTaskLog method.
	 */
	@Test
	void testSetCurrentTaskLog() {
		project = new Project("MA305");

		project.addCategoryLog("Homework");
		project.addCategoryLog("Notes");
		project.addCategoryLog("Labs");

		project.setCurrentTaskLog("All Tasks");
		assertEquals("All Tasks", project.getCurrentLog().getName());
		assertTrue(project.isChanged());

		project.setCurrentTaskLog("Homework");
		assertEquals("Homework", project.getCurrentLog().getName());
		assertTrue(project.isChanged());

		project.setCurrentTaskLog("Lecture");
		assertEquals("All Tasks", project.getCurrentLog().getName());
		assertTrue(project.isChanged());
	}

	/**
	 * Tests the editCategoryLogName method.
	 */
	@Test
	void testEditCategoryLogName() {
		project = new Project("MA305");

		IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
				() -> project.editCategoryLogName(null));
		assertEquals("Invalid name.", e.getMessage());

		IllegalArgumentException e1 = assertThrows(IllegalArgumentException.class,
				() -> project.editCategoryLogName(""));
		assertEquals("Invalid name.", e1.getMessage());

		IllegalArgumentException e2 = assertThrows(IllegalArgumentException.class,
				() -> project.editCategoryLogName("All Tasks"));
		assertEquals("Invalid name.", e2.getMessage());

		project.setCurrentTaskLog("All Tasks");
		IllegalArgumentException e3 = assertThrows(IllegalArgumentException.class,
				() -> project.editCategoryLogName("Change"));
		assertEquals("The All Tasks log may not be edited.", e3.getMessage());

		project.addCategoryLog("Notes");
		project.addCategoryLog("Labs");
		project.addCategoryLog("Homework");
		project.editCategoryLogName("Math Homework");

		String[] categoryNames = project.getCategoryNames();

		assertEquals(4, categoryNames.length);
		assertEquals("All Tasks", categoryNames[0]);
		assertEquals("Notes", categoryNames[1]);
		assertEquals("Labs", categoryNames[2]);
		assertEquals("Math Homework", categoryNames[3]);
	}

	/**
	 * Tests the removeCategoryLog method.
	 */
	@Test
	void testRemoveCategoryLog() {
		project = new Project("MA305");
		project.addCategoryLog("Labs");

		project.setCurrentTaskLog("All Tasks");
		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> project.removeCategoryLog());
		assertEquals("The All Tasks log may not be deleted.", e.getMessage());

		project.setCurrentTaskLog("Labs");
		project.getCurrentLog().addTask(new Task("Task 1", 60, "Descripion 1"));
		project.getCurrentLog().addTask(new Task("Task 2", 60, "Descripion 2"));
		project.getCurrentLog().addTask(new Task("Task 3", 60, "Descripion 3"));
		assertEquals(3, project.getCurrentLog().getTaskCount());

		project.removeCategoryLog();
		String[] categoryNames = project.getCategoryNames();

		assertEquals(1, categoryNames.length);
		assertEquals("All Tasks", categoryNames[0]);
		assertTrue(project.isChanged());
		assertEquals(0, project.getCurrentLog().getTaskCount());
	}

	/**
	 * Tests the removeCategoryLog method for the All Tasks log.
	 */
	@Test
	void testRemoveCategoryLogAllTasks() {
		project = new Project("MA305");
		project.addCategoryLog("Labs");
		project.addCategoryLog("Homework");

		project.setCurrentTaskLog("Labs");
		project.addTask(new Task("Task 1", 60, "Descripion 1"));
		project.addTask(new Task("Task 2", 60, "Descripion 2"));
		project.addTask(new Task("Task 3", 60, "Descripion 3"));
		assertEquals(3, project.getCurrentLog().getTaskCount());

		project.setCurrentTaskLog("Homework");
		project.addTask(new Task("Task 4", 60, "Descripion 4"));
		project.addTask(new Task("Task 5", 60, "Descripion 5"));
		project.addTask(new Task("Task 6", 60, "Descripion 6"));
		assertEquals(3, project.getCurrentLog().getTaskCount());

		project.setCurrentTaskLog("All Tasks");
		assertEquals(6, project.getCurrentLog().getTaskCount());

		project.setCurrentTaskLog("Labs");
		project.removeCategoryLog();

		String[] categoryNames = project.getCategoryNames();
		assertEquals(2, categoryNames.length);
		assertEquals("All Tasks", categoryNames[0]);
		assertEquals("Homework", categoryNames[1]);
		assertTrue(project.isChanged());

		project.setCurrentTaskLog("All Tasks");
		assertEquals(3, project.getCurrentLog().getTaskCount());
	}

	/**
	 * Tests the editTask method.
	 */
	@Test
	void testEditTask() {
		project = new Project("MA305");
		assertThrows(IllegalArgumentException.class, () -> project.editTask(-1, null, -1, null));

		project.addTask(new Task("Task 1", 60, "Descripion 1"));
		project.addTask(new Task("Task 2", 60, "Descripion 2"));
		project.addTask(new Task("Task 3", 60, "Descripion 3"));

		project.editTask(1, "P1 Skeleton", 45, "Completed the skeleton for P1.");

		Task task = project.getCurrentLog().getTask(1);
		assertEquals("P1 Skeleton", task.getTaskTitle());
		assertEquals(45, task.getTaskDuration());
		assertEquals("Completed the skeleton for P1.", task.getTaskDetails());
		assertTrue(project.isChanged());
	}

	/**
	 * Tests the removeTask method.
	 */
	@Test
	void testInvalidRemoveTask() {
		project = new Project("MA305");

		assertThrows(IllegalArgumentException.class, () -> project.removeTask(-1));
		assertThrows(IllegalArgumentException.class, () -> project.removeTask(3));
	}

	/**
	 * Tests the removeTask method with valid values.
	 */
	@Test
	void testRemoveTask() {
		project = new Project("MA305");

		project.addTask(new Task("Task 1", 60, "Descripion 1"));
		project.addTask(new Task("Task 2", 60, "Descripion 2"));
		project.addTask(new Task("Task 3", 60, "Descripion 3"));

		assertEquals(3, project.getCurrentLog().getTaskCount());

		project.setCurrentTaskLog("All Tasks");
		project.removeTask(1);
		assertEquals(2, project.getCurrentLog().getTaskCount());

		assertEquals("Task 1", project.getCurrentLog().getTask(0).getTaskTitle());
		assertEquals("Task 3", project.getCurrentLog().getTask(1).getTaskTitle());
		project.setCurrentTaskLog("All Tasks");
		assertEquals("Task 1", project.getCurrentLog().getTask(0).getTaskTitle());
		assertEquals("Task 3", project.getCurrentLog().getTask(1).getTaskTitle());

		assertTrue(project.isChanged());
	}
}
