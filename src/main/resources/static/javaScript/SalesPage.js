document.addEventListener('DOMContentLoaded', () => {
    const itemBody = document.getElementById('salesItemTableBody');
    const productSearch = document.getElementById('productSearch');
    const productResult = document.getElementById('productResult');
    const money = value => `\u20b9${Number(value || 0).toFixed(2)}`;
    const displayDate = value => {
        const parts = String(value || '').substring(0, 10).split('-');
        return parts.length === 3 ? `${parts[2]}-${parts[1]}-${parts[0]}` : '';
    };
    let barcodeTimer;
    let barcodeRequestId = 0;
    let historySales = [];
    let selectedHistorySale = null;

    if (!itemBody || !productSearch) {
        return;
    }

    function recalculate() {
        let subtotal = 0;
        let itemCount = 0;
        itemBody.querySelectorAll('.sale-item-row').forEach((row, index) => {
            row.querySelector('.row-number').textContent = index + 1;
            const quantity = Number(row.querySelector('.qty').value) || 0;
            const price = Number(row.querySelector('.price').value) || 0;
            row.querySelector('.line-total').textContent = money(quantity * price);
            subtotal += quantity * price;
            itemCount += quantity;
        });
        const discount = Number(document.getElementById('discountAmount').value) || 0;
        const tax = Number(document.getElementById('taxAmount').value) || 0;
        const total = Math.max(0, subtotal - discount + tax);
        document.getElementById('itemCount').textContent = itemCount;
        document.getElementById('subtotal').textContent = money(subtotal);
        document.getElementById('grandTotal').textContent = money(total);
        document.getElementById('totalAmount').value = total.toFixed(2);
    }

    function reindexRows() {
        itemBody.querySelectorAll('.sale-item-row').forEach((row, index) => {
            row.querySelector('.row-number').textContent = index + 1;
            row.querySelectorAll('input').forEach(input => {
                input.name = input.name.replace(/salesItems\[\d+\]/, `salesItems[${index}]`);
            });
        });
    }

    function getEmptyStarterRow() {
        const firstRow = itemBody.querySelector('.sale-item-row');
        return firstRow && !firstRow.querySelector('.item-id').value ? firstRow : null;
    }

    function applyProduct(row, product) {
        row.querySelector('.item-name').value = product.name ?? product.productName ?? '';
        row.querySelector('.item-id').value = product.id ?? product.productId ?? '';
        row.querySelector('.item-barcode').value = product.barcode ?? product.productBarCode ?? '';
        const quantity = row.querySelector('.qty');
        if (!quantity.value || Number(quantity.value) < 1) {
            quantity.value = '1';
        }
        row.querySelector('.price').value = product.pricePerUnit ?? product.productPrice ?? 0;
    }

    function addOrIncreaseItem(product) {
        const existingRow = [...itemBody.querySelectorAll('.sale-item-row')].find(row =>
            row.querySelector('.item-id').value === String(product.id ?? product.productId));
        if (existingRow && existingRow.querySelector('.item-id').value) {
            const quantity = existingRow.querySelector('.qty');
            const currentQuantity = Number(quantity.value) || 0;
            if (currentQuantity >= Number(product.availableStock)) {
                window.alert('The available inventory quantity is already in the bill');
                return;
            }
            quantity.value = String(currentQuantity + 1);
            recalculate();
            return;
        }
        addItem({
            id: product.id ?? product.productId,
            name: product.name ?? product.productName,
            pricePerUnit: product.pricePerUnit ?? product.productPrice,
            barcode: product.productBarCode
        });
    }

    function addItem(product) {
        const starterRow = product ? getEmptyStarterRow() : null;
        if (starterRow) {
            applyProduct(starterRow, product);
            recalculate();
            return;
        }
        const index = itemBody.querySelectorAll('.sale-item-row').length;
        const row = document.createElement('tr');
        row.className = 'sale-item-row';
        row.innerHTML = `<td class="row-number">${index + 1}</td>
            <td><input class="form-control item-name" name="salesItems[${index}].productInfo" readonly required>
            <input type="hidden" class="item-id" name="salesItems[${index}].productId">
            <input type="hidden" class="item-barcode" name="salesItems[${index}].barcode"></td>
            <td><input class="form-control qty" type="number" min="1" value="" name="salesItems[${index}].quantitySold" required></td>
            <td><input class="form-control price" type="number" min="0" step=".01" name="salesItems[${index}].unitPriceAtSale" required></td>
            <td>\u20b90.00</td><td class="line-total">\u20b90.00</td>
            <td><button type="button" class="btn btn-danger btn-sm remove-item" title="Remove item" aria-label="Remove item"><i class="bi bi-trash"></i> Delete</button></td>`;
        if (product) {
            applyProduct(row, product);
        }
        itemBody.appendChild(row);
        recalculate();
    }

    async function searchProducts() {
        const query = productSearch.value.trim();
        if (query.length < 2) {
            productResult.style.display = 'none';
            return;
        }
        try {
            const response = await fetch(`/api/product/search?query=${encodeURIComponent(query)}`);
            if (!response.ok) throw new Error(`Product search failed: ${response.status}`);
            const products = await response.json();
            productResult.innerHTML = '';
            if (!products.length) {
                productResult.textContent = 'No products found';
            } else {
                products.slice(0, 8).forEach(product => {
                    const button = document.createElement('button');
                    button.type = 'button';
                    button.className = 'btn btn-link text-left d-block w-100 product-choice';
                    button.textContent = `${product.name} - ${money(product.pricePerUnit)} - stock ${product.stockOnHeand}`;
                    button.addEventListener('click', () => {
                        if (!product.stockOnHeand || product.stockOnHeand < 1) {
                            window.alert('Stock not available');
                            return;
                        }
                        addItem(product);
                        productResult.style.display = 'none';
                        productSearch.value = '';
                    });
                    productResult.appendChild(button);
                });
            }
            productResult.style.display = 'block';
        } catch (error) {
            console.error(error);
            productResult.textContent = 'Unable to search products';
            productResult.style.display = 'block';
        }
    }

    async function lookupBarcode() {
        const barcode = productSearch.value.trim();
        if (!/^\d{4,}$/.test(barcode)) {
            return;
        }
        const requestId = ++barcodeRequestId;
        try {
            const response = await fetch(`/sale/api/getproduct/barcode/${encodeURIComponent(barcode)}`);
            if (!response.ok) throw new Error(`Barcode lookup failed: ${response.status}`);
            const product = await response.json();
            if (requestId !== barcodeRequestId || barcode !== productSearch.value.trim()) {
                return;
            }
            if (!product.productId) {
                productResult.textContent = 'Barcode not found in price book';
                productResult.style.display = 'block';
                return;
            }
            if (Number(product.availableStock) < 1) {
                window.alert('Stock not available in inventory');
                return;
            }
            addOrIncreaseItem(product);
            productResult.style.display = 'none';
            productSearch.value = '';
        } catch (error) {
            console.error(error);
            productResult.textContent = 'Unable to find barcode';
            productResult.style.display = 'block';
        }
    }

    itemBody.addEventListener('input', recalculate);
    itemBody.addEventListener('click', event => {
        const removeButton = event.target.closest('.remove-item');
        if (removeButton) {
            removeButton.closest('.sale-item-row').remove();
            if (!itemBody.querySelector('.sale-item-row')) {
                addItem();
            }
            reindexRows();
            recalculate();
        }
    });
    document.getElementById('addItem').addEventListener('click', () => addItem());
    function resetBill() {
        itemBody.innerHTML = '';
        addItem();
        document.getElementById('discountAmount').value = '';
        document.getElementById('taxAmount').value = '';
        productSearch.value = '';
        productResult.innerHTML = '';
        productResult.style.display = 'none';
        recalculate();
    }
    document.getElementById('clearItems').addEventListener('click', resetBill);
    document.getElementById('discountAmount').addEventListener('input', recalculate);
    document.getElementById('taxAmount').addEventListener('input', recalculate);
    document.getElementById('searchButton').addEventListener('click', searchProducts);
    productSearch.addEventListener('input', () => {
        window.clearTimeout(barcodeTimer);
        if (/^\d{4,}$/.test(productSearch.value.trim())) {
            barcodeTimer = window.setTimeout(lookupBarcode, 700);
        }
    });
    productSearch.addEventListener('keydown', event => {
        if (event.key === 'Enter') {
            event.preventDefault();
            if (productSearch.value.trim().length >= 4) {
                lookupBarcode();
            } else {
                searchProducts();
            }
        }
    });

    document.querySelectorAll('.sales-tab').forEach(tab => tab.addEventListener('click', () => {
        document.querySelectorAll('.sales-tab').forEach(item => item.classList.remove('active'));
        document.querySelectorAll('.sales-panel').forEach(panel => panel.style.display = 'none');
        tab.classList.add('active');
        document.getElementById(tab.dataset.tab).style.display = 'block';
        if (tab.dataset.tab === 'sale-history') loadHistory();
    }));

    async function loadHistory() {
        const response = await fetch('/sale/api/history');
        const sales = await response.json();
        historySales = sales;
        const term = document.getElementById('historySearch').value.toLowerCase();
        const payment = document.getElementById('paymentFilter').value;
        const from = document.getElementById('fromDate').value;
        const to = document.getElementById('toDate').value;
        const filtered = sales.filter(sale => {
            const date = String(sale.saleDate).substring(0, 10);
            return (!term || String(sale.billNumber || sale.saleId).includes(term) || (sale.customerName || '').toLowerCase().includes(term) || (sale.customerMobile || '').includes(term)) &&
                (!payment || sale.paymentMethod === payment) && (!from || date >= from) && (!to || date <= to);
        });
        document.getElementById('historyBody').innerHTML = filtered.length ? filtered.map(sale =>
            `<tr><td><strong>#${sale.billNumber || sale.saleId}</strong></td><td>${displayDate(sale.saleDate)}</td><td>${sale.customerName || 'Walk-in customer'}</td><td>${sale.customerMobile || '-'}</td><td>${sale.itemCount}</td><td><strong>${money(sale.totalAmount)}</strong></td><td>${sale.paymentMethod || '-'}</td><td><span class="${sale.itemCount === 0 ? 'returned' : sale.paymentMethod === 'Credit' ? 'due' : 'paid'}">${sale.itemCount === 0 ? 'Return' : sale.paymentMethod === 'Credit' ? 'Due' : 'Paid'}</span></td><td><button type="button" class="btn btn-gold btn-sm history-view" data-sale-id="${sale.saleId}" title="View sale"><i class="bi bi-eye mr-1"></i>View</button></td></tr>`).join('') : '<tr><td colspan="9" class="text-center text-muted py-4">No sales found</td></tr>';
    }

    function escapeHtml(value) {
        return String(value ?? '').replace(/[&<>'"]/g, character => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', "'": '&#39;', '"': '&quot;' }[character]));
    }

    function renderHistoryReceipt(sale) {
        const items = (sale.items || []).map((item, index) => {
            const amount = (Number(item.quantitySold) || 0) * (Number(item.unitPriceAtSale) || 0);
            return `<tr><td>${index + 1}</td><td>${escapeHtml(item.productInfo)}</td><td class="receipt-qty">${item.quantitySold || 0}</td><td class="receipt-price">${Number(item.unitPriceAtSale || 0).toFixed(2)}</td><td class="receipt-total">${amount.toFixed(2)}</td></tr>`;
        }).join('');
        const discount = Number(sale.discountAmount || 0);
        const tax = Number(sale.taxAmount || 0);
        const subtotal = Number(sale.totalAmount || 0) + discount - tax;
        const status = sale.itemCount === 0 ? 'Return' : sale.paymentMethod === 'Credit' ? 'Due' : 'Paid';
        return `<div class="receipt-paper"><div class="receipt-brand"><img src="/img/das_fasion_logo.jpeg" alt="DAS logo"><h1>DAS</h1><p>fit &amp; fashion</p><p>Pro: Susanta Das<br>At+ PO: Chhatinasole<br>Dist: Jhargram<br>Mobile: 8670116323</p></div><div class="receipt-meta"><span>Bill: <strong>${escapeHtml(sale.billNumber || sale.saleId)}</strong></span><span>${escapeHtml(displayDate(sale.saleDate))}</span></div><div class="receipt-customer"><strong>Customer:</strong> ${escapeHtml(sale.customerName || 'Walk-in customer')}<br>${escapeHtml(sale.customerMobile || '')}</div><table class="receipt-table"><thead><tr><th>#</th><th>Item</th><th class="receipt-qty">Qty</th><th class="receipt-price">Price</th><th class="receipt-total">Amount</th></tr></thead><tbody>${items}</tbody></table><div class="receipt-summary"><div><span>Subtotal</span><span>${money(subtotal)}</span></div><div><span>Discount</span><span>${money(discount)}</span></div><div><span>Tax</span><span>${money(tax)}</span></div><div class="receipt-grand"><span>Grand Total</span><span>${money(sale.totalAmount)}</span></div><div><span>Payment</span><strong>${escapeHtml(sale.paymentMethod || 'Cash')}</strong></div><div><span>Status</span><strong>${status}</strong></div></div><div class="receipt-footer"><strong>Thank you for shopping with us!</strong>Please visit again.</div></div>`;
    }

    function showModal(modal) {
        if (window.jQuery && window.jQuery.fn && window.jQuery.fn.modal) {
            window.jQuery(modal).modal('show');
        } else if (window.bootstrap && window.bootstrap.Modal) {
            window.bootstrap.Modal.getOrCreateInstance(modal).show();
        }
    }

    function hideModal(modal) {
        if (window.jQuery && window.jQuery.fn && window.jQuery.fn.modal) {
            window.jQuery(modal).modal('hide');
        } else if (window.bootstrap && window.bootstrap.Modal) {
            window.bootstrap.Modal.getOrCreateInstance(modal).hide();
        }
    }

    function printReceiptInNewTab(receiptMarkup) {
        const printWindow = window.open('', '_blank');
        if (!printWindow) {
            window.alert('Please allow pop-ups to print the bill.');
            return;
        }
        printWindow.document.write(`<!doctype html><html><head><title>Bill</title><style>
            @page{size:80mm auto;margin:0}html,body{width:80mm;margin:0;background:#fff;color:#111}
            .receipt-paper{width:80mm;box-sizing:border-box;margin:0;padding:4mm;background:#fff;font:10px Arial,sans-serif}
            .receipt-brand{display:grid;grid-template-columns:17mm 1fr;column-gap:3mm;align-items:center;text-align:left;border-bottom:1px dashed #777;padding-bottom:4px;margin-bottom:5px}
            .receipt-brand img{grid-row:1/span 3;width:17mm;height:17mm;object-fit:contain;border-radius:50%}.receipt-brand h1{margin:0;font-size:16px;letter-spacing:.6px}.receipt-brand p{margin:1px 0;font-size:8.5px;line-height:1.2}
            .receipt-meta{display:flex;justify-content:space-between;gap:8px;font-size:10px;margin-bottom:7px}.receipt-customer{border-bottom:1px dashed #777;padding-bottom:5px;margin-bottom:6px;font-size:10px}
            .receipt-table{width:100%;border-collapse:collapse;font-size:10px}.receipt-table th{border-bottom:1px solid #111;padding:4px 0;text-align:left}.receipt-table td{padding:4px 0;vertical-align:top}.receipt-table th:last-child,.receipt-table td:last-child,.receipt-price,.receipt-total{text-align:right}.receipt-summary{border-top:1px dashed #777;margin-top:5px;padding-top:5px;font-size:10px}.receipt-summary div{display:flex;justify-content:space-between;padding:2px 0}.receipt-grand{border-top:1px solid #111;border-bottom:1px double #111;margin-top:3px;padding:6px 0!important;font-size:14px;font-weight:bold}.receipt-footer{border-top:1px dashed #777;margin-top:8px;padding-top:7px;text-align:center;font-size:10px}.receipt-footer strong{display:block;font-size:12px;margin-bottom:3px}
        </style></head><body>${receiptMarkup}</body></html>`);
        printWindow.document.close();
        printWindow.addEventListener('load', () => {
            printWindow.focus();
            printWindow.print();
        });
    }

    function openHistoryDetails(sale) {
        selectedHistorySale = sale;
        const modal = document.getElementById('historyDetailsModal');
        document.getElementById('historyDetailsContent').innerHTML = renderHistoryReceipt(sale);
        showModal(modal);
    }

    document.getElementById('historyBody').addEventListener('click', event => {
        const button = event.target.closest('.history-view');
        if (!button) return;
        const sale = historySales.find(item => String(item.saleId) === button.dataset.saleId);
        if (sale) openHistoryDetails(sale);
    });
    document.getElementById('historyPrintButton').addEventListener('click', () => {
        printReceiptInNewTab(document.getElementById('historyDetailsContent').innerHTML);
    });
    document.getElementById('adjustSaleButton').addEventListener('click', () => {
        const returnRows = document.getElementById('returnRows');
        const additionRows = document.getElementById('additionRows');
        returnRows.innerHTML = '';
        additionRows.innerHTML = '';
        addReturnRow();
        addAdditionRow();
        document.getElementById('adjustPaymentMethod').value = selectedHistorySale?.paymentMethod || 'Cash';
        document.getElementById('adjustSaleMessage').className = 'alert d-none mt-3 mb-0';
        hideModal(document.getElementById('historyDetailsModal'));
        showModal(document.getElementById('adjustSaleModal'));
    });
    function adjustmentMessage(text, type = 'warning') {
        const message = document.getElementById('adjustSaleMessage');
        message.textContent = text;
        message.className = `alert alert-${type} mt-3 mb-0`;
    }

    function updateAdjustmentSummary() {
        const original = Number(selectedHistorySale?.totalAmount) || 0;
        const returned = [...document.querySelectorAll('.adjustment-return-row')].reduce((sum, row) => {
            return sum + ((Number(row.querySelector('.adjustment-quantity')?.value) || 0)
                * (Number(row.querySelector('.adjustment-price')?.value) || 0));
        }, 0);
        const additions = [...document.querySelectorAll('.adjustment-add-row')].reduce((sum, row) => {
            return sum + ((Number(row.querySelector('.adjustment-quantity')?.value) || 0)
                * (Number(row.querySelector('.adjustment-price')?.value) || 0));
        }, 0);
        const finalAmount = Math.max(0, original - returned + additions);
        const difference = finalAmount - original;
        document.getElementById('adjustmentOriginalTotal').textContent = money(original);
        document.getElementById('adjustmentReturnTotal').textContent = `-${money(returned)}`;
        document.getElementById('adjustmentAdditionTotal').textContent = money(additions);
        document.getElementById('adjustmentFinalTotal').textContent = money(finalAmount);

        const paymentDifference = document.getElementById('adjustmentPaymentDifference');
        paymentDifference.classList.remove('collect', 'refund');
        if (difference > 0) {
            paymentDifference.classList.add('collect');
            paymentDifference.textContent = `Amount to collect: ${money(difference)}`;
        } else if (difference < 0) {
            paymentDifference.classList.add('refund');
            paymentDifference.textContent = `Amount to refund: ${money(Math.abs(difference))}`;
        } else {
            paymentDifference.classList.add('collect');
            paymentDifference.textContent = 'No additional payment';
        }
    }

    async function lookupAdjustmentBarcode(row, returnRow) {
        const input = row.querySelector('.adjustment-barcode');
        const barcode = input.value.trim();
        if (!barcode) return;
        try {
            const response = await fetch(`/sale/api/getproduct/barcode/${encodeURIComponent(barcode)}`);
            if (!response.ok) throw new Error('Barcode lookup failed');
            const product = await response.json();
            if (!product.productId) throw new Error('Barcode not found in price book');
            if (returnRow) {
                const item = (selectedHistorySale?.items || []).find(candidate =>
                    String(candidate.barcode || '').trim() === barcode);
                if (!item) {
                    row.dataset.saleItemId = '';
                    row.querySelector('.adjustment-product').value = '';
                    row.querySelector('.adjustment-sold-quantity').value = '';
                    row.querySelector('.adjustment-quantity').value = '';
                    row.querySelector('.adjustment-quantity').disabled = true;
                    row.querySelector('.adjustment-price').value = '';
                    adjustmentMessage('This barcode is not an item in the selected bill.', 'danger');
                    return;
                }
                row.dataset.saleItemId = item.saleItemId;
                row.querySelector('.adjustment-product').value = item.productInfo;
                row.querySelector('.adjustment-sold-quantity').value = item.quantitySold;
                row.querySelector('.adjustment-quantity').max = item.quantitySold;
                row.querySelector('.adjustment-quantity').value = 1;
                row.querySelector('.adjustment-quantity').disabled = false;
                row.querySelector('.adjustment-price').value = Number(item.unitPriceAtSale || 0).toFixed(2);
            } else {
                row.querySelector('.adjustment-product').value = product.productName || '';
                row.querySelector('.adjustment-price').value = Number(product.productPrice || 0).toFixed(2);
                row.querySelector('.adjustment-quantity').value = 1;
                row.querySelector('.adjustment-quantity').disabled = false;
                row.dataset.productId = product.productId;
            }
            recalculateAdjustmentRow(row);
        } catch (error) {
            row.querySelector('.adjustment-product').value = '';
            row.querySelector('.adjustment-price').value = '';
            row.querySelector('.adjustment-quantity').disabled = true;
            adjustmentMessage(error.message, 'danger');
        }
    }

    function wireAdjustmentBarcode(row, returnRow) {
        const input = row.querySelector('.adjustment-barcode');
        const lookup = () => lookupAdjustmentBarcode(row, returnRow);
        let lookupTimer;
        const scheduleLookup = () => {
            window.clearTimeout(lookupTimer);
            lookupTimer = window.setTimeout(lookup, 250);
        };
        input.addEventListener('input', scheduleLookup);
        input.addEventListener('change', () => {
            window.clearTimeout(lookupTimer);
            lookup();
        });
        input.addEventListener('keydown', event => {
            if (event.key === 'Enter') {
                event.preventDefault();
                window.clearTimeout(lookupTimer);
                lookup();
            }
        });
        row.querySelector('.adjustment-quantity').addEventListener('input', () => recalculateAdjustmentRow(row));
        row.querySelector('.adjustment-price').addEventListener('input', () => recalculateAdjustmentRow(row));
    }

    function recalculateAdjustmentRow(row) {
        const quantity = Number(row.querySelector('.adjustment-quantity').value) || 0;
        const price = Number(row.querySelector('.adjustment-price').value) || 0;
        const total = row.querySelector('.adjustment-total');
        if (total) total.textContent = money(quantity * price);
        updateAdjustmentSummary();
    }

    function addReturnRow() {
        const row = document.createElement('tr');
        row.className = 'adjustment-return-row';
        row.innerHTML = `<td><input class="form-control adjustment-barcode" placeholder="Scan barcode"></td><td><input class="form-control adjustment-product" readonly></td><td><input class="form-control adjustment-sold-quantity" type="number" readonly></td><td><input class="form-control adjustment-quantity" type="number" min="1" disabled></td><td><input class="form-control adjustment-price" type="number" min="0" step=".01" readonly></td><td><div class="adjustment-total">${money(0)}</div></td><td><button type="button" class="btn btn-gold btn-sm remove-adjustment-row" title="Remove return item" aria-label="Remove return item">Remove</button></td>`;
        document.getElementById('returnRows').appendChild(row);
        wireAdjustmentBarcode(row, true);
        updateAdjustmentSummary();
    }
    function addAdditionRow() {
        const row = document.createElement('tr');
        row.className = 'adjustment-add-row';
        row.innerHTML = `<td><input class="form-control adjustment-barcode" placeholder="Scan barcode"></td><td><input class="form-control adjustment-product" readonly></td><td><input class="form-control adjustment-quantity" type="number" min="1" disabled></td><td><input class="form-control adjustment-price" type="number" min="0" step=".01" readonly></td><td><div class="adjustment-total">${money(0)}</div></td><td><button type="button" class="btn btn-gold btn-sm remove-adjustment-row" title="Remove added item" aria-label="Remove added item">Remove</button></td>`;
        document.getElementById('additionRows').appendChild(row);
        wireAdjustmentBarcode(row, false);
        updateAdjustmentSummary();
    }
    document.getElementById('addReturnRow').addEventListener('click', addReturnRow);
    document.getElementById('addAdditionRow').addEventListener('click', event => {
        event.preventDefault();
        addAdditionRow();
    });
    document.getElementById('adjustSaleModal').addEventListener('click', event => {
        const remove = event.target.closest('.remove-adjustment-row');
        if (remove) {
            remove.closest('tr').remove();
            updateAdjustmentSummary();
        }
    });
    document.getElementById('saveAdjustmentButton').addEventListener('click', async () => {
        if (!selectedHistorySale) return;
        const message = document.getElementById('adjustSaleMessage');
        const returns = [...document.querySelectorAll('.adjustment-return-row')].map(row => ({
            saleItemId: row.dataset.saleItemId ? Number(row.dataset.saleItemId) : null,
            barcode: row.querySelector('.adjustment-barcode').value.trim(),
            quantity: Number(row.querySelector('.adjustment-quantity').value) || 0,
            unitPrice: row.querySelector('.adjustment-price').value ? Number(row.querySelector('.adjustment-price').value) : null
        })).filter(item => item.saleItemId && item.barcode && item.quantity > 0);
        const additions = [...document.querySelectorAll('.adjustment-add-row')].map(row => ({
            barcode: row.querySelector('.adjustment-barcode').value.trim(),
            quantity: Number(row.querySelector('.adjustment-quantity').value) || 0,
            unitPrice: row.querySelector('.adjustment-price').value ? Number(row.querySelector('.adjustment-price').value) : null
        })).filter(item => item.barcode && item.quantity > 0);
        const returnedQuantities = new Map();
        for (const item of returns) {
            returnedQuantities.set(item.saleItemId, (returnedQuantities.get(item.saleItemId) || 0) + item.quantity);
        }
        for (const [saleItemId, quantity] of returnedQuantities) {
            const saleItem = (selectedHistorySale.items || []).find(item => Number(item.saleItemId) === Number(saleItemId));
            if (saleItem && quantity > Number(saleItem.quantitySold || 0)) {
                adjustmentMessage(`Cannot return more than ${saleItem.quantitySold} for ${saleItem.productInfo}.`, 'danger');
                return;
            }
        }
        if (!returns.length && !additions.length) {
            message.textContent = 'Add at least one return or new item.';
            message.className = 'alert alert-warning mt-3 mb-0';
            return;
        }
        const response = await fetch(`/sale/api/adjust/${selectedHistorySale.saleId}`, {
            method: 'POST', headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ returns, additions, paymentMethod: document.getElementById('adjustPaymentMethod').value })
        });
        const result = await response.json();
        if (!response.ok || result.status !== 'success') {
            message.textContent = result.message || 'Adjustment failed.';
            message.className = 'alert alert-danger mt-3 mb-0';
            return;
        }
        hideModal(document.getElementById('adjustSaleModal'));
        await loadHistory();
        const updatedSale = historySales.find(sale => sale.saleId === selectedHistorySale.saleId);
        if (updatedSale) openHistoryDetails(updatedSale);
    });
    document.querySelectorAll('[data-dismiss="modal"]').forEach(button => button.addEventListener('click', () => {
        const modal = button.closest('.modal');
        if (modal) hideModal(modal);
    }));

    ['historySearch', 'fromDate', 'toDate', 'paymentFilter'].forEach(id => document.getElementById(id).addEventListener('input', loadHistory));
    document.getElementById('refreshHistory').addEventListener('click', loadHistory);
    const savedBillModal = document.getElementById('savedBillModal');
    if (savedBillModal) showModal(savedBillModal);
    if (savedBillModal) {
        document.getElementById('savedBillPrintButton').addEventListener('click', () => {
            printReceiptInNewTab(savedBillModal.querySelector('.receipt-paper').outerHTML);
            window.addEventListener('afterprint', resetBill, { once: true });
        });
    }
    recalculate();
});
