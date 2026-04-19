/**
 * Application services and thin facades.
 * <p>
 * Handlers in {@code main} depend on {@link service.MovieService},
 * {@link service.ConcessionService}, and {@link service.BookingService} for persistence.
 * {@link service.MovieManager} and {@link service.InventoryManager} are optional facades
 * for read-heavy or inventory-oriented flows without duplicating file logic.
 */
package service;
