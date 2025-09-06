document.addEventListener('DOMContentLoaded', () => {
    console.log('^^^^^^^^^^^^^^^^^^^^^^^^^^^^ callingnnnnnn')
    const tableBody = document.querySelector('#purchaseItemTable tbody');
    const barcodeSearchInput = document.getElementById('barcodeSearchInput');
    const scannedBarcodes = new Set();
    // --- DEBOUNCE FUNCTION ---
    function debounce(func, delay) {
        let timeout;
        return function (...args) {
            const context = this;
            clearTimeout(timeout);
            timeout = setTimeout(() => func.apply(context, args), delay);
        };
    }


    let purchaseItemTable;
    if (document.getElementById('purchaseItemTable')) {
        purchaseItemTable = document.getElementById('purchaseItemTable').getElementsByTagName('tbody')[0];
    }
    const totalAmountInput = document.getElementById('purchaseTotalAmount');
    //add logic to update Total Amount automatic
    const updateTotalAmount = () => {
        console.log('update sum calling.....')
        let total = 0;
        // Get all rows in the table body
        if (purchaseItemTable && purchaseItemTable.querySelectorAll('tr')) {
            const rows = purchaseItemTable.querySelectorAll('tr');
            rows.forEach(row => {
                const quantityInput = row.querySelector('.quantity-input');
                const priceInput = row.querySelector('.price-input');

                const quantity = parseFloat(quantityInput.value) || 0;
                const price = parseFloat(priceInput.value) || 0;

                total += quantity * price;
            });
            console.log('total  ------> ' + total)
            totalAmountInput.value = total;
        }

    };


    window.addPurchesItemRow = function () {
        const template = document.getElementById('purchase-item-row-template');
        if (!template) {
            console.error("Error: Template element with ID 'purchase-item-row-template' not found.");
            return;
        }
        const newRow = template.content.cloneNode(true).querySelector('tr');
        const newIndex = tableBody.children.length;
        newRow.id = `row-${newIndex}`;
        console.log('newIndex ' + newIndex);

        // Set the correct name attributes for the new row inputs
        newRow.querySelectorAll('input').forEach(input => {
            input.name = input.name.replace('INDEX_PLACEHOLDER_X', newIndex);
        });

        // Attach event listener to the new row's barcode input
        const newBarcodeInput = newRow.querySelector('.barcode-input');
        newBarcodeInput.addEventListener('keyup', handleBarcodeInput);

        // Attach event listener to the remove button
        const removeButton = newRow.querySelector('.remove-button');
        removeButton.onclick = () => removeRow(newIndex);
        if (newIndex === 0) {
            removeButton.style.display = 'none';
        }

        tableBody.appendChild(newRow);
        updateTotalAmount();
    }

    // Function to remove a row from the table
    window.removeRow = function (indexToRemove) {
        const row = document.getElementById('row-' + indexToRemove);
        if (row) {
            row.remove();
            // Re-index all the remaining rows to maintain binding
            reindexRows();
            updateTotalAmount()
        }

    }

    if (purchaseItemTable) {
        // Using event delegation to listen for changes and removals on the table
        purchaseItemTable.addEventListener('input', (event) => {
            // Check if the event target is a quantity or price input
            if (event.target.classList.contains('quantity-input') || event.target.classList.contains('price-input')) {
                updateTotalAmount();
            }
        });

        // Using event delegation to listen for clicks on remove buttons
        purchaseItemTable.addEventListener('click', (event) => {
            if (event.target.classList.contains('remove-button')) {
                const rowId = event.target.closest('tr').id;
                removeRow(rowId);
            }
        });
    }



    // Function to re-index rows after a removal
    function reindexRows() {
        if (tableBody) {
            const rows = tableBody.children;
            for (let i = 0; i < rows.length; i++) {
                const row = rows[i];
                row.id = 'row-' + i;
                row.querySelectorAll('input').forEach(input => {
                    input.name = input.name.replace(/items\[\d+\]/, `items[${i}]`);
                });

                const removeButton = row.querySelector('.remove-button');
                if (removeButton) {
                    removeButton.onclick = () => window.removeRow(i);
                    if (i === 0) {
                        removeButton.style.display = 'none';
                    } else {
                        removeButton.style.display = 'inline-block';
                    }
                }
            }
        }

    }



    // Initial re-indexing and search setup for existing rows
    reindexRows();
    // Initial calculation on page load
    updateTotalAmount();

    if (tableBody) {
        tableBody.addEventListener('keyup', (event) => {
            if (event.target.classList.contains('barcode-input')) {
                handleBarcodeInput(event);
            }
        });
    }


    // --- New Logic: Search by Price Book Barcode ---
    const handleBarcodeInput = debounce((event) => {
        event.preventDefault();
        const barcode = event.target.value.trim();
        const currentRow = event.target.closest('tr');
        if (barcode != '' && barcode.length >= 4) {
            if (scannedBarcodes.has(barcode)) {
                showToast('Duplicate Found', 'This product has already been added.', 'warning');
                event.target.value = '';
                return;
            }
            console.log('barcode --> ' + barcode)
            fetch(`/sale/api/getproduct/barcode/${barcode}`, { method: 'GET' }).then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            }).then(product => {
                console.log('res ' + JSON.stringify(product))
                if (product && product.id == null) {
                    showToast('Product not Found. ', `Product is not found with this Barcode-${barcode}, Please add pricebook with is barcode`, 'warning')
                } else {
                    renderProductInfoToCurrentSec(product, currentRow);
                    scannedBarcodes.add(barcode);
                    updateTotalAmount();
                    document.getElementById('purchaseAddRowButton').click()
                    showMessageBox('Success', 'Product successfully Fund!', 'success');
                }

            }).catch(err => {
                console.log('err ')
                showToast('Error ', JSON.stringify(err), 'error')
            })

        }


    }, 800);



    function renderProductInfoToCurrentSec(foundProduct, currentRow) {
        const productNameInput = currentRow.querySelector('.product-search-input');
        const productIdInput = currentRow.querySelector('.product-id-input-hidden');
        const priceInput = currentRow.querySelector('.price-input');

        productNameInput.value = foundProduct.productName;
        productIdInput.value = foundProduct.productId;
        priceInput.value = foundProduct.productPrice.toFixed(2);
    }

    document.getElementById("purchaseForm").addEventListener("keydown", function (event) {
        if (event.key === "Enter" && event.target.tagName !== "TEXTAREA") {
            event.preventDefault();
        }
    });

    function showToast(title, message, type = "primary") {
        // Get toast elements
        const toastEl = document.getElementById("basicToast");
        const toastTitle = document.getElementById("myToastTitle");
        const toastMessage = document.getElementById("myToastMessage");
        const header = toastEl.querySelector(".toast-header");

        // Set values
        toastTitle.textContent = title;
        toastMessage.textContent = message;

        // Update color (Bootstrap contextual classes)
        header.className = `toast-header bg-${type} text-light`;

        // Show toast
        const toast = new bootstrap.Toast(toastEl);
        toast.show();
    }

});