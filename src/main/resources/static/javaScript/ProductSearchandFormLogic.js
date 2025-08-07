document.addEventListener('DOMContentLoaded', () => {
    const tableBody = document.querySelector('#purchaseItemTable tbody');

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
        }
    }

    // Function to re-index rows after a removal
    function reindexRows() {
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

    // Initial re-indexing and search setup for existing rows
    reindexRows();
    tableBody.querySelectorAll('.product-search-input').forEach(input => {
        initializeSearch(input);
    });
});