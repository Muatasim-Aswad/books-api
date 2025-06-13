package com.asim.business.common.exception;

import lombok.Getter;

/**
 * Exception to be thrown when an authenticated user tries to access a resource or perform an action
 * they don't have permission for.
 * Should result in a 403 Forbidden response.
 */
@Getter
public class ForbiddenAccessException extends RuntimeException {
  private final String resource;
  private final String action;
  private final String reason;

  /**
   * Creates a new ForbiddenAccessException.
   *
   * @param resource The resource the user is trying to access
   * @param action The action the user is trying to perform
   * @param reason The reason why access is denied
   */
  public ForbiddenAccessException(String resource, String action, String reason) {
    super(String.format("Access denied: Cannot %s %s. Reason: %s", action, resource, reason));
    this.resource = resource;
    this.action = action;
    this.reason = reason;
  }

  /**
   * Creates a new ForbiddenAccessException with just a reason.
   *
   * @param reason The reason why access is denied
   */
  public ForbiddenAccessException(String reason) {
    super("Access denied: " + reason);
    this.resource = null;
    this.action = null;
    this.reason = reason;
  }
}