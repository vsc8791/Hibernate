package com.phoenix.demos;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class TestApp {
	   private static SessionFactory factory; 
	   @SuppressWarnings({ "unchecked", "unused" })
	public static void main(String[] args) {
	         factory = new Configuration().configure().buildSessionFactory();
	         Session ses=factory.openSession();
	      TestApp app = new TestApp();

	      /* Add few employee records in database */
	     /* Integer empID1 = app.addEmployee("XYZ", "X", 2000);
	      Integer empID2 = app.addEmployee("ABC", "Y", 5000);
	      Integer empID3 = app.addEmployee("PQR", "Z", 5000);
	      Integer empID4 = app.addEmployee("MNO", "ZZZZ", 3000);*/

	      /* List down all the employees */
	      //app.listEmployees();

	      /* Print Total employee's count */
	      //app.countEmployee();

/*	       Print Total salary 
	      app.totalSalary();*/
	      
	      // Perform the required operation
	      Criteria cr=ses.createCriteria(Employee.class);
	      cr=app.performOperation(cr);
	      ArrayList<Employee> output=(ArrayList<Employee>) cr.list();
	      
	      for(Employee e:output)
	      {
	    	  System.out.println(e.getFirstName());
	      }
	   }
	   /* Method to CREATE an employee in the database */
	   public Integer addEmployee(String fname, String lname, int salary){
	      Session session = factory.openSession();
	      Transaction tx = null;
	      Integer employeeID = null;
	      try{
	         tx = session.beginTransaction();
	         Employee employee = new Employee(fname, lname, salary);
	         employeeID = (Integer) session.save(employee); 
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	      }
	      return employeeID;
	   }

	   public Criteria performOperation(Criteria cr)
	   {
//		   Criteria cr = session.createCriteria(Employee.class);

		// To get records having salary more than 2000
		
		
		   cr.add(Restrictions.gt("salary", 2000));
		  

		// To get records having salary less than 2000
		//cr.add(Restrictions.lt("salary", 2000));

		// To get records having fistName starting with zara
		//cr.add(Restrictions.like("firstName", "zara%"));

		// Case sensitive form of the above restriction.
		//cr.add(Restrictions.ilike("firstName", "zara%"));

		// To get records having salary in between 1000 and 2000
		//cr.add(Restrictions.between("salary", 1000, 2000));

		// To check if the given property is null
		//cr.add(Restrictions.isNull("salary"));

		// To check if the given property is not null
		//cr.add(Restrictions.isNotNull("salary"));

		// To check if the given property is empty
		//cr.add(Restrictions.isEmpty("salary"));

		// To check if the given property is not empty
		//cr.add(Restrictions.isNotEmpty("salary"));
		
		Criterion salary = Restrictions.gt("salary", 2000);
		Criterion name = Restrictions.ilike("firstName","zara%");
		Criterion between = Restrictions.between("firstName","A", "B");

		// To get records matching with OR condistions
		//LogicalExpression orExp = Restrictions.or(salary, name);
		//cr.add( orExp );


		// To get records matching with AND condistions
		LogicalExpression andExp = Restrictions.and(salary, name);
		LogicalExpression threeAnd = Restrictions.and(andExp, between);
		cr.add( threeAnd );
		
		return cr;
	   }
	   
	   /* Method to  READ all the employees having salary more than 2000 */
	   @SuppressWarnings("unchecked")
	public void listEmployees( ){
	      Session session = factory.openSession();
	      Transaction tx = null;
	      try{
	         tx = session.beginTransaction();
	         Criteria cr = session.createCriteria(Employee.class);
	         // Add restriction.
	         cr.add(Restrictions.gt("salary", 4000));
	         List<Employee> employees = (List<Employee>)cr.list();

	         for (Iterator<Employee> iterator = 
	                           employees.iterator(); iterator.hasNext();){
	            Employee employee = (Employee) iterator.next(); 
	            System.out.print("First Name: " + employee.getFirstName()); 
	            System.out.print("  Last Name: " + employee.getLastName()); 
	            System.out.println("  Salary: " + employee.getSalary()); 
	         }
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	      }
	   }
	   /* Method to print total number of records */
	   public void countEmployee(){
	      Session session = factory.openSession();
	      Transaction tx = null;
	      try{
	         tx = session.beginTransaction();
	         Criteria cr = session.createCriteria(Employee.class);

	         // To get total row count.
	         cr.setProjection(Projections.rowCount());
	         List rowCount = cr.list();
	       
	         System.out.println("Total Coint: " + rowCount.get(0) );
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	      }
	   }
	  /* Method to print sum of salaries */
	   public void totalSalary(){
	      Session session = factory.openSession();
	      Transaction tx = null;
	      try{
	         tx = session.beginTransaction();
	         Criteria cr = session.createCriteria(Employee.class);

	         // To get total salary.
	         cr.setProjection(Projections.sum("salary"));
	         List totalSalary = cr.list();

	         System.out.println("Total Salary: " + totalSalary.get(0) );
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	      }
	   }
}
