package com.heaven.study.activiti;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath:activiti_cfg.xml"})
public class MyTest {

	private static final Logger log = LoggerFactory.getLogger(MyTest.class);
	private ProcessEngine processEngine;

	@Before
	public void before() {

//		ProcessEngineConfiguration.createProcessEngineConfigurationFromResource() //这个方法加载的xml无法注入properties占位符
		ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath:activiti_cfg.xml");
		ProcessEngineConfiguration processEngineConfiguration = (ProcessEngineConfiguration) classPathXmlApplicationContext.getBean("standaloneProcessEngineConfiguration");
		processEngine = processEngineConfiguration.buildProcessEngine();
		log.info("初始化完成");
	}


	/**
	 * 创建一个流程
	 */
	@Test
	public void test02() {

		RepositoryService repositoryService = processEngine.getRepositoryService();

		Deployment deployment = repositoryService.createDeployment()
				.addClasspathResource("MyProcess.bpmn")
				.name("测试流程1")
				.category("测试")
				.deploy();
	}

	@Test
	public void test03() {

		RuntimeService runtimeService = processEngine.getRuntimeService();

		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("myProcess");

		log.info(processInstance.getName());

	}


	@Test
	public void test04() {

		TaskService taskService = processEngine.getTaskService();
		TaskQuery taskQuery = taskService.createTaskQuery();
		List<Task> taskList = taskQuery.taskAssignee("任务执行者1").list();
		for(Task t: taskList) {
			log.info("name={}, createTime={}",t.getName(), t.getCreateTime());
		}

	}


	@Test
	public void test05() {

		TaskService taskService = processEngine.getTaskService();

		TaskQuery taskQuery = taskService.createTaskQuery();
		List<Task> taskList = taskQuery.taskAssignee("任务执行者1").list();
		for(Task t: taskList) {
			taskService.complete(t.getId());
		}

		log.info("完成任务！");

	}


	@Test
	public void test06() {

		TaskService taskService = processEngine.getTaskService();

		TaskQuery taskQuery = taskService.createTaskQuery();
		List<Task> taskList = taskQuery.taskAssignee("任务执行者2").list();
		for(Task t: taskList) {
			taskService.complete(t.getId());
		}

		log.info("完成任务！");

	}


	@Test
	public void test07() {

		List<ProcessDefinition> list = processEngine.getRepositoryService().createProcessDefinitionQuery().latestVersion().list();

		processEngine.getRepositoryService();
		processEngine.getTaskService();
		processEngine.getRuntimeService();
		processEngine.getDynamicBpmnService();
		processEngine.getHistoryService();
		processEngine.getManagementService();


		for(ProcessDefinition pd: list) {
			log.info("id:{}, name:{}, version:{}", pd.getId(), pd.getName(), pd.getVersion());
		}

	}

}
