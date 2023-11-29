package ac.at.univie.blockchain.objects;

/**
 * The ItemTransaction Class is the kind of itemsTransactions
 * that will be transfered on blocks in the blockchain. This
 * usecase is a library where authors can register their books.
 * The books have author name, book name and adding date of the book.
 */
public class ItemTransaction {
    private String AuthorName;
    private String BookName;
    private String bookAddingDate;
    private String ISBN;
    private String port;

    public ItemTransaction() {
    }

    public ItemTransaction(String authorName, String bookName, String bookAddingDate, String ISBN) {
        this.AuthorName = authorName;
        this.BookName = bookName;
        this.bookAddingDate = bookAddingDate;
        this.ISBN = ISBN;
    }

    public String getAuthorName() {
        return AuthorName;
    }

    public void setAuthorName(String authorName) {
        AuthorName = authorName;
    }

    public String getBookName() {
        return BookName;
    }

    public void setBookName(String bookName) {
        BookName = bookName;
    }

    public String getBookAddingDate() {
        return bookAddingDate;
    }

    public void setBookAddingDate(String bookAddingDate) {
        this.bookAddingDate = bookAddingDate;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String iSBN) {
        ISBN = iSBN;
    }

    public String getPort() {
        return this.port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
