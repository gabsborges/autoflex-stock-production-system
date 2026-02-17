package com.autoflex.service;

import com.autoflex.entity.ProductRawMaterial;
import com.autoflex.entity.RawMaterial;
import com.autoflex.entity.StockMovement;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import org.jboss.logging.Logger;

@ApplicationScoped
public class StockAdjustmentService {

    private static final Logger LOG = Logger.getLogger(StockAdjustmentService.class);

    /**
     * Consumes the required quantity of raw materials for a product.
     *
     * @param productRawMaterial the mapping of product to raw material
     * @param quantityProduced   the number of products being produced
     */
    @Transactional
    public void consumeRawMaterials(ProductRawMaterial productRawMaterial, int quantityProduced) {
        RawMaterial rawMaterial = productRawMaterial.rawMaterial;

        // Calculate total quantity needed
        int requiredQuantity = productRawMaterial.quantityRequired * quantityProduced;

        // Determine actual quantity to consume (cannot exceed available stock)
        int consumableQuantity = Math.min(requiredQuantity, rawMaterial.quantity);

        if (consumableQuantity <= 0) {
            LOG.warnf("Insufficient stock for raw material '%s'. Available: %d, Required: %d",
                    rawMaterial.name, rawMaterial.quantity, requiredQuantity);
            return; // Nothing to consume
        }

        // Create stock movement record
        StockMovement movement = new StockMovement();
        movement.product = productRawMaterial.product;
        movement.rawMaterial = rawMaterial;
        movement.type = StockMovement.MovementType.OUT;
        movement.quantity = consumableQuantity;
        movement.movementDate = LocalDateTime.now();
        movement.persist();

        // Update raw material stock
        rawMaterial.quantity -= consumableQuantity;

        // Persist updated stock (allow zero)
        rawMaterial.persist();

        LOG.infov("Consumed {0} units of raw material {1}. Remaining stock: {2}",
                consumableQuantity, rawMaterial.name, rawMaterial.quantity);
    }

    /**
     * Produces a product by consuming its raw materials.
     *
     * @param productRawMaterial the mapping of product to raw material
     * @param quantityProduced   the number of products being produced
     */
    @Transactional
    public void produceProduct(ProductRawMaterial productRawMaterial, int quantityProduced) {
        consumeRawMaterials(productRawMaterial, quantityProduced);
        // Here you could also persist the produced product if needed
    }
}
