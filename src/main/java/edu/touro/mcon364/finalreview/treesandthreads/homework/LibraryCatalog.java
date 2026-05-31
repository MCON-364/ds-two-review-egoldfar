package edu.touro.mcon364.finalreview.treesandthreads.homework;

import edu.touro.mcon364.finalreview.treesandthreads.model.Book;
import java.util.*;
import java.util.stream.*;

/**
 * Homework 1 - Library Catalog (TreeMap + TreeSet + Streams)
 *
 * Scenario: a library stores books. Each book has a title, author, and
 * publication year. The catalog must answer several questions in sorted order.
 *
 * Before coding, think about:
 * - Which structure gives us books sorted by title automatically?
 * - Should the author-to-books index use a List or a Set inside the map?
 *   What happens if the same book appears twice?
 * - What does NavigableMap.headMap give us, and when would we use it?
 *
 * Requirements:
 * - The constructor receives the list of books to index.
 * - buildTitleIndex() returns a TreeMap keyed by title for O(log n) exact lookups.
 * - buildAuthorIndex() returns a TreeMap grouping books by author; books for each
 *   author are sorted by title.
 * - getBooksPublishedBefore(year) returns all books published strictly before
 *   the given year, sorted by title.
 * - getAuthorsWithMoreThan(n) returns a sorted list of author names who have
 *   more than n books in the catalog.
 * - findByTitlePrefix(prefix) returns all books whose title starts with the
 *   given string, alphabetically. Use NavigableMap range operations.
 *
 * Do not use explicit loops. Use streams and collectors.
 */
public class LibraryCatalog {

    private final List<Book> books;

    public LibraryCatalog(List<Book> books) {
        // validate non-null, store a defensive copy
        if (books == null) throw new IllegalArgumentException("Books list cannot be null");
        this.books = List.copyOf(books);
    }

    /**
     * Returns a TreeMap keyed by book title for O(log n) exact lookups.
     * If two books share a title, keep only one (your choice which).
     *
     */
    public TreeMap<String, Book> buildTitleIndex() {
        return books.stream()
        .distinct()
        .collect(Collectors.toMap(
            Book::title,
            book -> book,
            (existing, replacement) -> existing, // keep first if duplicate title
            TreeMap::new
        ));
    }

    /**
     * Returns a TreeMap grouping books by author; each author maps to a
     * TreeSet of their books sorted by title.
     */
    public TreeMap<String, TreeSet<Book>> buildAuthorIndex() {
        return books.stream()
        .collect(Collectors.groupingBy(
            Book::author,
            TreeMap::new,
            Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Book::title)))
        ));
    }

    /**
     * Returns all books published strictly before the given year, sorted by title.
     *
     */
    public List<Book> getBooksPublishedBefore(int year) {
        return books.stream()
        .filter(book -> book.year() < year)
        .sorted(Comparator.comparing(Book::title))
        .toList();
    }

    /**
     * Returns a sorted list of author names who have more than n books in this catalog.
     *
     */
    public List<String> getAuthorsWithMoreThan(int n) {
        var authorMap = buildAuthorIndex();
        return authorMap.entrySet().stream()
        .filter(a -> a.getValue().size() > n)
        .map(a -> a.getKey())
        .toList();
    }

    /**
     * Returns all books whose title starts with the given prefix, alphabetically.
     *
     */
    public List<Book> findByTitlePrefix(String prefix) {
        return books.stream()
        .filter(b -> b.title().startsWith(prefix))
        .sorted()
        .toList();
    }
}

