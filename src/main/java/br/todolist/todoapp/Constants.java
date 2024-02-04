package br.todolist.todoapp;

import java.util.ArrayList;
import java.util.List;

public class Constants {
    public static final List<String> WHITE_LIST_URIS = new ArrayList<>(
            List.of("/", "/auth/login", "/auth/logout", "/auth/register"));
}
