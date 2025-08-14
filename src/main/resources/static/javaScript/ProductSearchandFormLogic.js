document.addEventListener('DOMContentLoaded', () => {
    const tableBody = document.querySelector('#purchaseItemTable tbody');

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

    // Generic function to initialize search functionality on an input
    function initializeSearch(input) {
        const searchResults = input.parentNode.querySelector('.search-results');
        const resultList = searchResults.querySelector('ul');
        const productIdInput = input.closest('tr').querySelector('.product-id-input-hidden');

        // Function to fetch and display search results
        input.addEventListener('input', async (e) => {
            const query = e.target.value;
            if (query.length < 2) {
                searchResults.style.display = 'none';
                return;
            }

            try {
                const response = await fetch(`/api/product/search?query=${query}`);
                const products = await response.json();

                resultList.innerHTML = '';
                if (products.length > 0) {
                    products.forEach(product => {
                        const li = document.createElement('li');
                        li.textContent = `${product.name}`;
                        li.addEventListener('click', () => {
                            input.value = product.name;
                            searchResults.style.display = 'none';
                            if (productIdInput) {
                                productIdInput.value = product.id;
                            }
                        });
                        resultList.appendChild(li);
                    });
                    searchResults.style.display = 'block';
                } else {
                    searchResults.style.display = 'none';
                }
            } catch (error) {
                console.error('Error fetching products:', error);
                searchResults.style.display = 'none';
            }
        });

        // Hide search results when clicking outside
        document.addEventListener('click', (e) => {
            if (!input.contains(e.target) && !searchResults.contains(e.target)) {
                searchResults.style.display = 'none';
            }
        });
    }

    /**
     * The fix for the TypeError is here:
     * The template element is now fetched inside the function to ensure it always exists when called.
     */
    window.addPurchesItemRow = function () {
        const template = document.getElementById('purchase-item-row-template');
        if (!template) {
            console.error("Error: Template element with ID 'purchase-item-row-template' not found.");
            return;
        }
        const newRow = template.content.cloneNode(true).querySelector('tr');
        const newIndex = tableBody.children.length;

        newRow.querySelectorAll('input').forEach(input => {
            input.name = input.name.replace('INDEX_PLACEHOLDER_X', newIndex);
        });
        newRow.id = 'row-' + newIndex;

        const removeButton = newRow.querySelector('.remove-button');
        removeButton.onclick = () => window.removeRow(newIndex);

        tableBody.appendChild(newRow);

        // Initialize search on the new input
        updateTotalAmount()
        const newInput = newRow.querySelector('.product-search-input');
        if (newInput) {
            initializeSearch(newInput);
        }
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
    if(tableBody){
        tableBody.querySelectorAll('.product-search-input').forEach(input => {
        initializeSearch(input);
    });
    }
    
});