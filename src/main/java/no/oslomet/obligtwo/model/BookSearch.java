package no.oslomet.obligtwo.model;

public class BookSearch {
    private String searchText;

    public BookSearch(){
    }

    public BookSearch(String searchText){
        this.searchText=searchText;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }
}
