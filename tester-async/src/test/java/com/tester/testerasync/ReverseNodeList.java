package com.tester.testerasync;

import org.junit.Test;

public class ReverseNodeList {
    @Test
    public void test_node(){
        ListNode listNode = new ListNode(1);
        ListNode listNode1 = new ListNode(2);
        ListNode listNode2 = new ListNode(3);
        ListNode listNode21 = new ListNode(31);
        ListNode listNode22 = new ListNode(32);
        ListNode listNode23 = new ListNode(33);
        ListNode listNode24 = new ListNode(43);
        listNode.next = listNode1;
        listNode1.next = listNode2;
        listNode2.next = listNode21;
        listNode21.next = listNode22;
        listNode22.next = listNode23;
        listNode23.next = listNode24;
        ListNode reverseList = ReverseList(listNode);
        do{
            System.out.println(reverseList.val);
        }while ((reverseList = reverseList.next) != null);
    }


    public ListNode ReverseList(ListNode head) {
        if(head == null || head.next == null){
            return head;
        }
        ListNode last = head;
        ListNode temp1 = last;
        ListNode temp2 = last.next;
        do{
            last.next = last.next.next;
            temp2.next = temp1;
            temp1 = temp2;
            temp2 = last.next;
        }while (last.next != null);
        return temp1;
    }
    public class ListNode {
        int val;
        ListNode next = null;
        ListNode(int val) {
            this.val = val;
        }
    }
}
