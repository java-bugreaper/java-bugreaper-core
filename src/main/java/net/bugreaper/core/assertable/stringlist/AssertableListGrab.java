package net.bugreaper.core.assertable.stringlist;


import java.util.List;

public interface AssertableListGrab {

    /**
     * Grab last element from AssertableStringList
     *
     * @return String with last element
     */
    String grabLastElement();


    /**
     * Grab all elements to list
     *
     * @return List(String) with all elements
     */
    List<String> grabLikeList();

}
