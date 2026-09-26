package it.verde.model.type;

public enum OrderStatus {
    OPEN,
    CONFIRMED,
    SHIPPED,
    DELIVERED,
    CANCELLED;

    public boolean canTransitionTo(OrderStatus nextStatus) {
        if (nextStatus == null || this == nextStatus) return false;
        return switch (this) {
            case OPEN -> nextStatus == CONFIRMED || nextStatus == CANCELLED;
            case CONFIRMED -> nextStatus == SHIPPED || nextStatus == CANCELLED;
            case SHIPPED -> nextStatus == DELIVERED || nextStatus == CANCELLED;
            case DELIVERED, CANCELLED -> false;
        };
    }
}
