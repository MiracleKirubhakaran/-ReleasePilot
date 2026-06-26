package com.cts.jfs.sprinboot.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    // ---------------------------------------------------
    // 1. Resource Not Found - 404
    // Triggered when any record is not found in DB
    // ---------------------------------------------------
    @ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView handleResourceNotFoundException(
            ResourceNotFoundException ex,
            HttpServletRequest request) {

        ModelAndView mav = new ModelAndView("error/404");
        mav.addObject("errorCode", HttpStatus.NOT_FOUND.value());
        mav.addObject("errorTitle", "Resource Not Found");
        mav.addObject("errorMessage", ex.getMessage());
        mav.addObject("requestUrl", request.getRequestURI());
        mav.addObject("timestamp", LocalDateTime.now());
        return mav;
    }

    // ---------------------------------------------------
    // 2. Duplicate Record - 409
    // Triggered when duplicate email, product code etc.
    // ---------------------------------------------------
    @ExceptionHandler(DuplicateRecordException.class)
    public ModelAndView handleDuplicateRecordException(
            DuplicateRecordException ex,
            HttpServletRequest request) {

        ModelAndView mav = new ModelAndView("error/409");
        mav.addObject("errorCode", HttpStatus.CONFLICT.value());
        mav.addObject("errorTitle", "Duplicate Record");
        mav.addObject("errorMessage", ex.getMessage());
        mav.addObject("requestUrl", request.getRequestURI());
        mav.addObject("timestamp", LocalDateTime.now());
        return mav;
    }

    // ---------------------------------------------------
    // 3. Invalid Operation - 400
    // Triggered for invalid business logic operations
    // e.g. publishing a note that is not Approved yet
    // ---------------------------------------------------
    @ExceptionHandler(InvalidOperationException.class)
    public ModelAndView handleInvalidOperationException(
            InvalidOperationException ex,
            HttpServletRequest request) {

        ModelAndView mav = new ModelAndView("error/400");
        mav.addObject("errorCode", HttpStatus.BAD_REQUEST.value());
        mav.addObject("errorTitle", "Invalid Operation");
        mav.addObject("errorMessage", ex.getMessage());
        mav.addObject("requestUrl", request.getRequestURI());
        mav.addObject("timestamp", LocalDateTime.now());
        return mav;
    }

    // ---------------------------------------------------
    // 4. Unauthorized Access - 401
    // Triggered when user is not authenticated
    // ---------------------------------------------------
    @ExceptionHandler(UnauthorizedException.class)
    public ModelAndView handleUnauthorizedException(
            UnauthorizedException ex,
            HttpServletRequest request) {

        ModelAndView mav = new ModelAndView("error/401");
        mav.addObject("errorCode", HttpStatus.UNAUTHORIZED.value());
        mav.addObject("errorTitle", "Unauthorized Access");
        mav.addObject("errorMessage", ex.getMessage());
        mav.addObject("requestUrl", request.getRequestURI());
        mav.addObject("timestamp", LocalDateTime.now());
        return mav;
    }

    // ---------------------------------------------------
    // 5. Access Denied - 403
    // Triggered when user does not have required role
    // ---------------------------------------------------
    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ModelAndView handleAccessDeniedException(
            org.springframework.security.access.AccessDeniedException ex,
            HttpServletRequest request) {

        ModelAndView mav = new ModelAndView("error/403");
        mav.addObject("errorCode", HttpStatus.FORBIDDEN.value());
        mav.addObject("errorTitle", "Access Denied");
        mav.addObject("errorMessage", "You do not have permission to access this resource.");
        mav.addObject("requestUrl", request.getRequestURI());
        mav.addObject("timestamp", LocalDateTime.now());
        return mav;
    }

    // ---------------------------------------------------
    // 6. Illegal Argument - 400
    // Triggered when wrong enum values or bad parameters
    // ---------------------------------------------------
    @ExceptionHandler(IllegalArgumentException.class)
    public ModelAndView handleIllegalArgumentException(
            IllegalArgumentException ex,
            HttpServletRequest request) {

        ModelAndView mav = new ModelAndView("error/400");
        mav.addObject("errorCode", HttpStatus.BAD_REQUEST.value());
        mav.addObject("errorTitle", "Bad Request");
        mav.addObject("errorMessage", ex.getMessage());
        mav.addObject("requestUrl", request.getRequestURI());
        mav.addObject("timestamp", LocalDateTime.now());
        return mav;
    }

    // ---------------------------------------------------
    // 7. NullPointerException - 500
    // Triggered when null values are encountered
    // ---------------------------------------------------
    @ExceptionHandler(NullPointerException.class)
    public ModelAndView handleNullPointerException(
            NullPointerException ex,
            HttpServletRequest request) {

        ModelAndView mav = new ModelAndView("error/500");
        mav.addObject("errorCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
        mav.addObject("errorTitle", "Internal Server Error");
        mav.addObject("errorMessage", "A null value was encountered. Please contact support.");
        mav.addObject("requestUrl", request.getRequestURI());
        mav.addObject("timestamp", LocalDateTime.now());
        return mav;
    }

    // ---------------------------------------------------
    // 8. Generic / Fallback Exception - 500
    // Catches any unhandled exception
    // ---------------------------------------------------
    @ExceptionHandler(Exception.class)
    public ModelAndView handleGenericException(
            Exception ex,
            HttpServletRequest request) {

        ModelAndView mav = new ModelAndView("error/500");
        mav.addObject("errorCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
        mav.addObject("errorTitle", "Internal Server Error");
        mav.addObject("errorMessage", "Something went wrong: " + ex.getMessage());
        mav.addObject("requestUrl", request.getRequestURI());
        mav.addObject("timestamp", LocalDateTime.now());
        return mav;
    }
}
