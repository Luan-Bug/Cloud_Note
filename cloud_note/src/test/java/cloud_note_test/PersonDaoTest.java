package cloud_note_test;

import org.junit.Before;

import cn.tedu.cloud_note.dao.PersonDao;
import cn.tedu.cloud_note.entity.Person;

public class PersonDaoTest extends Test{
	PersonDao dao;
	@Before
	public void init() {
		dao = ctx.getBean("personDao",PersonDao.class);
	}
	@org.junit.Test
	public void addPersontest() {
		Person person = new Person();
		person.setName("уехЩ");
		int n = dao.addPerson(person);
		System.out.println(n);
	}
}
