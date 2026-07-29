package net.bugreaper.core.assertable.stringlist;


import java.util.List;

public interface AssertableListGrab {

    /**
     * Returns the last element from the AssertableStringList.
     *
     * @return last element as a String
     */
    String grabLastElement();


    /**
     * Returns all elements from the AssertableStringList converted to a list.
     *
     * @return list containing all elements
     */
    List<String> grabLikeList();

}
