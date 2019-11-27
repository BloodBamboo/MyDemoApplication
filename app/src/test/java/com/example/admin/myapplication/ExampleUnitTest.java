package com.example.admin.myapplication;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
@RunWith(MockitoJUnitRunner.class)
public class ExampleUnitTest {

    private void println(Object o) {
        System.out.println(o);
    }

    @Test
    public void addition_isCorrect() throws Exception {
        System.out.println(System.currentTimeMillis());
        System.out.println("longToDate："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new Date(815602624)));//1529823960000L
    }

    @Test
    public void when_thenReturn(){
        //mock一个Iterator类
        Iterator iterator = Mockito.mock(Iterator.class);
        //预设当iterator调用next()时第一次返回hello，第n次都返回world
        Mockito.when(iterator.next()).thenReturn("hello").thenReturn("world");
        //使用mock的对象
        String result = iterator.next() + " " + iterator.next() + " " + iterator.next();
        //验证结果
        Assert.assertEquals("hello world world",result);
    }

    @Test()
    public void when_thenThrow() {
        Account account=Mockito.mock(Account.class,Mockito.RETURNS_DEEP_STUBS);
        Mockito.when(account.getRailwayTicket().getDestination()).thenReturn("Beijing");
        println(account.getRailwayTicket().getDestination());
        Mockito.verify(account.getRailwayTicket()).getDestination();
        Assert.assertEquals("Beijing",account.getRailwayTicket().getDestination());
    }

    public class RailwayTicket{
        private String destination;

        public String getDestination() {
            return destination;
        }

        public void setDestination(String destination) {
            this.destination = destination;
        }
    }

    public class Account{
        private RailwayTicket railwayTicket;

        public RailwayTicket getRailwayTicket() {
            return railwayTicket;
        }

        public void setRailwayTicket(RailwayTicket railwayTicket) {
            this.railwayTicket = railwayTicket;
        }
    }

    @Mock
    private List mockList;

    @Test
    public void deepstubsTest2(){
        Account account=Mockito.mock(Account.class);
        RailwayTicket railwayTicket=Mockito.mock(RailwayTicket.class);
        Mockito.when(account.getRailwayTicket()).thenReturn(railwayTicket);
        Mockito.when(railwayTicket.getDestination()).thenReturn("Beijing");

        println(account.getRailwayTicket().getDestination());
        Mockito.verify(account.getRailwayTicket()).getDestination();
        Assert.assertEquals("Beijing",account.getRailwayTicket().getDestination());
    }

    @Test
    public void shorthand(){
        mockList.add(2);
        Mockito.verify(mockList).add(2);
    }

    @Test
    public void with_arguments(){
        Comparable comparable = Mockito.mock(Comparable.class);
        //预设根据不同的参数返回不同的结果
        Mockito.when(comparable.compareTo("Test")).thenReturn(1);
        Mockito.when(comparable.compareTo("Omg")).thenReturn(2);
        Assert.assertEquals(1, comparable.compareTo("Test"));
        Assert.assertEquals(2, comparable.compareTo("Omg"));
        //对于没有预设的情况会返回默认值
        Assert.assertEquals(0, comparable.compareTo("Not stub"));
    }

    @Test
    public void with_unspecified_arguments(){
        List list = Mockito.mock(List.class);
        //匹配任意参数
        Mockito.when(list.get(Mockito.anyInt())).thenReturn(1);
        Mockito.when(list.contains(Mockito.argThat(new IsValid()))).thenReturn(true);
        Assert.assertEquals(1, list.get(1));
        Assert.assertEquals(1, list.get(999));
        Assert.assertTrue(list.contains(1));
        Assert.assertTrue(!list.contains(3));
    }
    private class IsValid implements ArgumentMatcher<Integer> {
        @Override
        public boolean matches(Integer o) {
            return o == 1 || o == 2;
        }
    }


    @Test
    public void all_arguments_provided_by_matchers(){
        Comparator comparator = Mockito.mock(Comparator.class);
        comparator.compare("nihao231213","hello");
        //如果你使用了参数匹配，那么所有的参数都必须通过matchers来匹配
        Mockito.verify(comparator).compare(Mockito.anyString(),Mockito.eq("hello"));
        //下面的为无效的参数匹配使用
        //verify(comparator).compare(anyString(),"hello");
    }

    @Test
    public void capturing_args(){
        PersonDao personDao = Mockito.mock(PersonDao.class);
        PersonService personService = new PersonService(personDao);

        ArgumentCaptor<Person> argument = ArgumentCaptor.forClass(Person.class);
        personService.update(1,"jack");
        Mockito.verify(personDao).update(argument.capture());
        Assert.assertEquals(1,argument.getValue().getId());
        Assert.assertEquals("jack",argument.getValue().getName());
    }

    class Person{
        private int id;
        private String name;

        Person(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    interface PersonDao{
        public void update(Person person);
    }

    class PersonService{
        private PersonDao personDao;

        PersonService(PersonDao personDao) {
            this.personDao = personDao;
        }

        public void update(int id,String name){
            personDao.update(new Person(id,name));
        }
    }

    @Test
    public void answerTest(){
        Mockito.when(mockList.get(Mockito.anyInt())).thenAnswer(new CustomAnswer());
        Assert.assertEquals("hello world:0",mockList.get(0));
        Assert.assertEquals("hello world:999",mockList.get(999));
    }

    private class CustomAnswer implements Answer<String> {
        @Override
        public String answer(InvocationOnMock invocation) throws Throwable {
            Object[] args = invocation.getArguments();
            return "hello world:"+args[0];
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void spy_on_real_objects(){
        List list = new LinkedList();
        List spy = Mockito.spy(list);
        //下面预设的spy.get(0)会报错，因为会调用真实对象的get(0)，所以会抛出越界异常
        //when(spy.get(0)).thenReturn(3);

        //使用doReturn-when可以避免when-thenReturn调用真实对象api
        Mockito.doReturn(999).when(spy).get(999);
        //预设size()期望值
        Mockito.when(spy.size()).thenReturn(100);
        //调用真实对象的api
        spy.add(1);
        spy.add(2);
        Assert.assertEquals(100,spy.size());
        Assert.assertEquals(1,spy.get(0));
        Assert.assertEquals(2,spy.get(1));
        Mockito.verify(spy).add(1);
        Mockito.verify(spy).add(2);
        Assert.assertEquals(999,spy.get(999));
        spy.get(2);
    }

    @Test
    public void real_partial_mock(){
        //通过spy来调用真实的api
        List list = Mockito.spy(new ArrayList());
        Assert.assertEquals(0,list.size());
        A a  = Mockito.mock(A.class);
        //通过thenCallRealMethod来调用真实的api
        Mockito.when(a.doSomething(Mockito.anyInt())).thenCallRealMethod();
        Assert.assertEquals(999,a.doSomething(999));
    }


    class A{
        public int doSomething(int i){
            return i;
        }
    }

    @Test
    public void reset_mock(){
        List list = Mockito.mock(List.class);
        Mockito.when(list.size()).thenReturn(10);
        list.add(1);
        Assert.assertEquals(10,list.size());
        //重置mock，清除所有的互动和预设
        Mockito.reset(list);
        Assert.assertEquals(0,list.size());
    }

    @Test
    public void verifying_number_of_invocations(){
        List list = Mockito.mock(List.class);
        list.add(1);
        list.add(2);
        list.add(2);
        list.add(3);
        list.add(3);
        list.add(3);
        //验证是否被调用一次，等效于下面的times(1)
        Mockito.verify(list).add(1);
        Mockito.verify(list,Mockito.times(1)).add(1);
        //验证是否被调用2次
        Mockito.verify(list,Mockito.times(2)).add(2);
        //验证是否被调用3次
        Mockito.verify(list,Mockito.times(3)).add(3);
        //验证是否从未被调用过
        Mockito.verify(list,Mockito.never()).add(4);
        //验证至少调用一次
        Mockito.verify(list,Mockito.atLeastOnce()).add(1);
        //验证至少调用2次
        Mockito.verify(list,Mockito.atLeast(2)).add(2);
        //验证至多调用3次
        Mockito.verify(list,Mockito.atMost(3)).add(3);
    }

    @Test(expected = RuntimeException.class)
    public void consecutive_calls(){
        //模拟连续调用返回期望值，如果分开，则只有最后一个有效
        Mockito.when(mockList.get(0)).thenReturn(0);
        Mockito.when(mockList.get(0)).thenReturn(1);
        Mockito.when(mockList.get(0)).thenReturn(2);
        Mockito.when(mockList.get(1)).thenReturn(0).thenReturn(1).thenThrow(new RuntimeException());
        Assert.assertEquals(2,mockList.get(0));
        Assert.assertEquals(2,mockList.get(0));
        Assert.assertEquals(0,mockList.get(1));
        Assert.assertEquals(1,mockList.get(1));
        //第三次或更多调用都会抛出异常
        mockList.get(1);
    }

    @Test
    public void verification_in_order(){
        List list = Mockito.mock(List.class);
        List list2 = Mockito.mock(List.class);
        list.add(1);
        list2.add("hello");
        list.add(2);
        list2.add("world");
        //将需要验证执行顺序的mock对象放入InOrder
        InOrder inOrder = Mockito.inOrder(list,list2);
        //下面的代码不能颠倒顺序，验证执行顺序
        inOrder.verify(list).add(1);
        inOrder.verify(list2).add("hello");
        inOrder.verify(list).add(2);
        inOrder.verify(list2).add("world");
    }

    @Test
    public void verify_interaction(){
        List list = Mockito.mock(List.class);
        List list2 = Mockito.mock(List.class);
        List list3 = Mockito.mock(List.class);
        list.add(1);
        Mockito.verify(list).add(1);
        Mockito.verify(list,Mockito.never()).add(2);
        //验证零互动行为
        Mockito.verifyZeroInteractions(list2,list3);
    }

    @Test(expected = NoInteractionsWanted.class)
    public void find_redundant_interaction(){
        List list = Mockito.mock(List.class);
        list.add(1);
        list.add(2);
        Mockito.verify(list,Mockito.times(2)).add(Mockito.anyInt());
        //检查是否有未被验证的互动行为，因为add(1)和add(2)都会被上面的anyInt()验证到，所以下面的代码会通过
        Mockito.verifyNoMoreInteractions(list);

        List list2 = Mockito.mock(List.class);
        list2.add(1);
        list2.add(2);
        Mockito.verify(list2).add(1);
        //检查是否有未被验证的互动行为，因为add(2)没有被验证，所以下面的代码会失败抛出异常
        Mockito.verifyNoMoreInteractions(list2);
    }




}