package org.example.util;

import org.example.entity.Role;
import org.example.entity.Status;
import org.example.exceptions.InvalidEnumValueException;

public class EnumUtil {
    public static Role parseRole(String roleString) {
        try {
            return Role.valueOf(roleString.toUpperCase());
        } catch (Exception e) {
            throw new InvalidEnumValueException("Invalid role: " + roleString);
        }
    }

    public static Status parseStatus(String statusString) {
        try {
            return Status.valueOf(statusString.toUpperCase());
        } catch (Exception e) {
            throw new InvalidEnumValueException("Недопустимый статус: " + statusString);
        }
    }
}

