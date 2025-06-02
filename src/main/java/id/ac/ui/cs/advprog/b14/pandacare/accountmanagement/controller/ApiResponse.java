package id.ac.ui.cs.advprog.b14.pandacare.accountmanagement.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private int StatusCode;
    private String Message;
    private T Result;
}