document.addEventListener("DOMContentLoaded", function () {
    // 오늘 날짜 표시
    const today = new Date();
    const days = ['일', '월', '화', '수', '목', '금', '토'];
    const formatted = `${today.getFullYear()}/${today.getMonth() + 1}/${today.getDate()}/${days[today.getDay()]}`;
    document.getElementById("today-date").textContent = formatted;
    document.getElementById("incomeDate").valueAsDate = new Date();
    document.getElementById("expenseDate").valueAsDate = new Date();
    loadCards();


    // 변수 선언
    let selectedType = null;
    let selectedDate = null;
    let selectedCategory = null;
    let selectedCard = null;
    let currentPage = 1;
    const itemsPerPage = 20;
    let allIncomeData = [];
    let allExpenseData = [];


    const selection = document.getElementById("selection");
    const incomeTable = document.getElementById("income-table");
    const expenseTable = document.getElementById("expense-table");

    const categoryBtn = document.getElementById("toggleCategory");
    const categoryList = document.getElementById("categoryList");

    const cardBtn = document.getElementById("cardChoice");
    const cardList = document.getElementById("cardList");

    const incomeBtn = document.getElementById("incomeBtn");
    const expenseBtn = document.getElementById("expenseBtn");

    const toggleBtn = document.getElementById("toggleBtn");
    const yearList = document.getElementById("yearList");
    const monthList = document.getElementById("monthList");
    const currentYear = new Date().getFullYear();
    const cardNum = document.getElementById("expenseCard").value;
    // 선택값 초기화 함수
    function initializeSelection() {
        const selected = selection.value;
        selectedType = selected;

        // 숨기기
        yearList.style.display = "none";
        monthList.style.display = "none";
        categoryList.style.display = "none";
        cardList.style.display = "none";

        if (selected === "income") {
            incomeTable.style.display = "table";
            expenseTable.style.display = "none";
            categoryBtn.style.display = "none";
            cardBtn.style.display = "none";
            incomeBtn.style.display = "inline-block";
            expenseBtn.style.display = "none";

        } else if (selected === "expense") {
            incomeTable.style.display = "none";
            expenseTable.style.display = "table";
            categoryBtn.style.display = "inline-block";
            cardBtn.style.display = "inline-block";
            incomeBtn.style.display = "none";
            expenseBtn.style.display = "inline-block";

        } else {
            incomeTable.style.display = "none";
            expenseTable.style.display = "none";
            categoryBtn.style.display = "none";
            cardBtn.style.display = "none";
            incomeBtn.style.display = "none";
            expenseBtn.style.display = "none";
        }

        fetchFilteredResults();
    }

    // 날짜 선택 (연도 → 월)
    toggleBtn.onclick = () => {
        yearList.style.display = "block";
        monthList.style.display = "none";
    };

    for (let i = 0; i < 5; i++) {
        const year = currentYear - i;
        const btn = document.createElement("button");
        btn.textContent = year;

        btn.onclick = () => {
            selectedDate = `${year}`;
            yearList.style.display = "none";
            renderMonths(year);
            monthList.style.display = "block";
        };

        yearList.appendChild(btn);
    }

    function renderMonths(year) {
        monthList.innerHTML = "";
        for (let i = 1; i <= 12; i++) {
            const btn = document.createElement("button");
            btn.textContent = `${i}월`;

            btn.onclick = () => {
                monthList.style.display = "none";
                selectedDate = `${year}-${String(i).padStart(2, '0')}`;
                fetchFilteredResults();
            };

            monthList.appendChild(btn);
        }
    }

    function loadCategoryFromDB() {
        categoryList.innerHTML = "";

        axios.get("/api/category")
            .then(response => {
                const categories = response.data;
                if (!categories || categories.length === 0) {
                    categoryList.innerHTML = "<p>카테고리가 없습니다.</p>";
                    return;
                }

                categories.forEach(category => {
                    const btn = document.createElement("button");
                    btn.textContent = category.category_name; // 또는 category.categoryName
                    btn.onclick = () => {
                        selectedCategory = category.category_name;
                        categoryList.style.display = "none";
                        fetchFilteredResults();
                    };
                    categoryList.appendChild(btn);
                });

                categoryList.style.display = "block";
                adjustScrollBoxHeight("categoryList");
            })
            .catch(error => {
                console.error("카테고리 불러오기 실패", error);
            });
    }
    axios.get('/api/bar')
        .then(response => {
            const user = response.data;
            console.log("사용자 이름:", user.name);
            const nameSpan = document.querySelector("#user-name");
            if (user.name) {
                nameSpan.textContent = user.name + '님';
            }
            if (user.photo) {
                const img = document.querySelector("#profile-img");
                img.src = user.photo;
                img.style.display = 'block';
            }
        })
        .catch(error => {
            console.error("사용자 정보 불러오기 실패:", error);
        });
// 클릭 이벤트에서 함수 호출만
    categoryBtn.addEventListener("click", () => {
        if (categoryList.style.display === "block") {
            categoryList.style.display = "none";
            return;
        }
        cardList.style.display = "none";
        loadCategoryFromDB(); // ✅ 호출만
    });

    function loadCardsFromDB() {
        cardList.innerHTML = ""; // 초기화

        axios.get(`/api/card`)
            .then(response => {
                const cards = response.data; // ✅ JSON 배열 직접 받음

                if (!cards || cards.length === 0) {
                    cardList.innerHTML = "<p>등록된 카드가 없습니다.</p>";
                    return;
                }

                cards.forEach(card => {
                    const bankName = card.linked_account?.bank_name || "-";  // snake_case 접근
                    const btn = document.createElement("button");
                    btn.textContent = `${card.card_name} (${bankName})`;
                    btn.value = card.card_id;

                    btn.onclick = () => {
                        selectedCard = card.card_name;  // 임시 대체
                        cardBtn.textContent = btn.textContent;
                        cardList.style.display = "none";
                        fetchFilteredResults();
                    };

                    cardList.appendChild(btn);
                });


                cardList.style.display = "block";
                adjustScrollBoxHeight("cardList");
            })
            .catch(error => {
                console.error("카드 불러오기 실패", error);
            });
    }

    // 카드 버튼 이벤트
    cardBtn.addEventListener("click", () => {
        if (cardList.style.display === "none" || cardList.style.display === "") {
            cardList.style.display = "block";
            categoryList.style.display = "none";
            loadCardsFromDB();  // ✅ 하드코딩 대신 DB 호출
        } else {
            cardList.style.display = "none";
        }
    });
    function fetchFilteredResults() {
        console.log("필터링 호출됨:", selectedType, selectedDate, selectedCategory, selectedCard);

        const params = {};
        if (selectedDate) params.date = selectedDate;
        if (selectedCategory) params.category = selectedCategory;
        if (selectedCard) params.card = selectedCard;

        if (selectedType === "income") {
            axios.get(`/api/incomeHistory`, { params })
                .then(response => {
                    allIncomeData = response.data;
                    currentPage = 1;
                    renderIncomePage();
                })
                .catch(error => {
                    console.error("수입 내역 불러오기 실패", error);
                });

        } else if (selectedType === "expense") {
            axios.get(`/api/expenseHistory`, { params })
                .then(response => {
                    allExpenseData = response.data;
                    currentPage = 1;
                    renderExpensePage();
                })
                .catch(error => {
                    console.error("소비 내역 불러오기 실패", error);
                });
        }
    }
    //페이지 함수
    function renderIncomePage() {
        const incomeBody = document.getElementById("income-body");
        incomeBody.innerHTML = "";

        const start = (currentPage - 1) * itemsPerPage;
        const end = start + itemsPerPage;
        const pageItems = allIncomeData.slice(start, end);

        pageItems.forEach(item => {
            const row = document.createElement("tr");
            row.innerHTML = `
            <td>${item.accountDay || "-"}</td>
            <td>${item.bankName || "-"}</td>
            <td>${item.source || "-"}</td>
            <td>${item.accountMoney != null && !isNaN(item.accountMoney) ? item.accountMoney.toLocaleString() : "-"}</td>
        `;
            incomeBody.appendChild(row);
        });

        renderPaginationControls(allIncomeData.length);
    }
    function renderExpensePage() {
        const expenseBody = document.getElementById("expense-body");
        expenseBody.innerHTML = "";

        const start = (currentPage - 1) * itemsPerPage;
        const end = start + itemsPerPage;
        const pageItems = allExpenseData.slice(start, end);

        pageItems.forEach(item => {
            const row = document.createElement("tr");
            row.innerHTML = `
            <td>${item.cardDay || "-"}</td>
            <td>${item.categoryName || "-"}</td>
            <td>${item.cardStoreName || "-"}</td>
            <td>${item.cardMoney != null ? item.cardMoney.toLocaleString() : "-"}</td>
            <td><button class="detail-btn" onclick="detailModal(${item.cardTradeId})">상세보기</button></td>
        `;
            expenseBody.appendChild(row);
        });

        renderPaginationControls(allExpenseData.length);
    }
    function renderPaginationControls(totalItems) {
        const paginationDiv = document.getElementById("pagination");
        paginationDiv.innerHTML = "";

        const totalPages = Math.ceil(totalItems / itemsPerPage);
        const maxButtonsToShow = 10;

        const group = Math.floor((currentPage - 1) / maxButtonsToShow);
        const startPage = group * maxButtonsToShow + 1;
        const endPage = Math.min(startPage + maxButtonsToShow - 1, totalPages);

        // < 이전 그룹
        if (startPage > 1) {
            const prev = document.createElement("button");
            prev.textContent = "<";
            prev.onclick = () => {
                currentPage = startPage - 1;
                selectedType === "income" ? renderIncomePage() : renderExpensePage();
            };
            paginationDiv.appendChild(prev);
        }

        // 페이지 번호들
        for (let i = startPage; i <= endPage; i++) {
            const btn = document.createElement("button");
            btn.textContent = i;
            btn.className = i === currentPage ? "active" : "";
            btn.onclick = () => {
                currentPage = i;
                selectedType === "income" ? renderIncomePage() : renderExpensePage();
            };
            paginationDiv.appendChild(btn);
        }

        // > 다음 그룹
        if (endPage < totalPages) {
            const next = document.createElement("button");
            next.textContent = ">";
            next.onclick = () => {
                currentPage = endPage + 1;
                selectedType === "income" ? renderIncomePage() : renderExpensePage();
            };
            paginationDiv.appendChild(next);
        }
    }


    // 가장 바깥에서 선언 (DOMContentLoaded 바깥 또는 window에 직접 등록)
    window.detailModal = function(cardTradeId) {
        const modal = document.getElementById("detailModal");
        if (!modal) {
            console.error("❌ detailModal 요소가 존재하지 않음");
            return;
        }

        axios.get(`/api/cardDetail/${cardTradeId}`)
            .then(response => {
                const data = response.data;
                console.log("🟢 상세 응답:", response.data);
                modal.style.display = "flex";

                document.getElementById("detail-card-name").value = data.cardName || "-";
                document.getElementById("detail-card-type").value = data.cardType || "-";
                document.getElementById("detail-card-number").value = data.cardNum || "-";
                document.getElementById("detail-date").value = data.cardDay || "-";
                document.getElementById("detail-cancel-date").value = data.cancel_day || "-";
                document.getElementById("detail-amount").value = data.cardMoney != null ? data.cardMoney.toLocaleString() : "-";
                document.getElementById("detail-store").value = data.cardStoreName || "-";
                document.getElementById("detail-category").value = data.categoryName || "-";
                document.getElementById("detail-biznum").value = data.businessNum || "-";
            })
            .catch(error => {
                console.error("상세 내역 불러오기 실패", error);
            });
    };
    function formatAccountNumber(accountNum) {
        if (!accountNum) return "";
        if (accountNum.length === 11) {
            return `${accountNum.slice(0, 3)}-${accountNum.slice(3, 7)}-${accountNum.slice(7)}`;
        } else if (accountNum.length === 10) {
            return `${accountNum.slice(0, 3)}-${accountNum.slice(3, 6)}-${accountNum.slice(6)}`;
        }
        return accountNum;
    }
    function maskAccountNumber(accountNum) {
        if (!accountNum) return "";

        // 숫자만 있을 경우 하이픈 자동 삽입
        const digitsOnly = accountNum.replace(/\D/g, '');
        if (digitsOnly.length === 10) {
            return `${digitsOnly.slice(0, 3)}-****-${digitsOnly.slice(6)}`;
        } else if (digitsOnly.length === 11) {
            return `${digitsOnly.slice(0, 3)}-****-${digitsOnly.slice(7)}`;
        }

        // 하이픈이 이미 포함된 경우 (123-4567-8901)
        const parts = accountNum.split("-");
        if (parts.length === 3) {
            return `${parts[0]}-****-${parts[2]}`;
        }

        // 예상되지 않은 포맷은 원본 반환
        return accountNum;
    }

    // 계좌 목록 로드 함수
    function loadAccounts() {
        axios.get("/api/account")
            .then(response => {
                const nameSelect = document.getElementById("bankAccountSelect");
                const numSelect = document.getElementById("bankAccountNumSelect");

                nameSelect.innerHTML = "<option value=''>계좌 이름 선택</option>";
                numSelect.innerHTML = "<option value=''>계좌 번호 선택</option>";
                numSelect.disabled = true;  // 초기에는 비활성화

                response.data.forEach(account => {
                    const nameOption = document.createElement("option");
                    nameOption.value = account.account_name;
                    nameOption.textContent = account.account_name;
                    nameOption.dataset.accountNum = account.account_num_masked;  // 실제 번호
                    nameOption.dataset.bankName = account.bank_name;
                    nameSelect.appendChild(nameOption);
                });

                nameSelect.addEventListener("change", function () {
                    const selectedOption = nameSelect.options[nameSelect.selectedIndex];
                    const matchedNum = selectedOption.dataset.accountNum;
                    const bankName = selectedOption.dataset.bankName;

                    numSelect.innerHTML = "";
                    if (matchedNum) {
                        const opt = document.createElement("option");
                        opt.value = matchedNum;
                        opt.textContent = maskAccountNumber(matchedNum); // ✅ 마스킹 적용
                        opt.dataset.accountName = selectedOption.value;
                        opt.dataset.bankName = bankName;
                        numSelect.appendChild(opt);
                        numSelect.disabled = false;
                    } else {
                        numSelect.disabled = true;
                    }
                });
            })
            .catch(error => {
                console.error("계좌 목록 불러오기 실패", error);
            });
    }
    document.getElementById("bankAccountSelect").addEventListener("change", function () {
        const selectedAccountName = this.value;
        const numSelect = document.getElementById("bankAccountNumSelect");

        for (let option of numSelect.options) {
            if (option.dataset.accountName === selectedAccountName) {
                option.selected = true;
                break;
            }
        }
    });

    document.getElementById("saveIncomeBtn").addEventListener("click", () => {
        const nameSelect = document.getElementById("bankAccountSelect");
        const numSelect = document.getElementById("bankAccountNumSelect");

        const selectedNameOption = nameSelect.options[nameSelect.selectedIndex];
        const selectedNumOption = numSelect.options[numSelect.selectedIndex];

        const bankAccountNum = selectedNumOption.value;
        const accountName = selectedNameOption.value;
        const bankName = selectedNumOption.dataset.bankName;
        const amount = document.getElementById("incomeAmount").value;

        if (!bankAccountNum || !accountName) {
            alert("계좌 이름과 번호를 선택하세요.");
            return;
        }

        if (!amount || isNaN(parseInt(amount))) {
            alert("입금 금액을 올바르게 입력하세요.");
            return;
        }

        const dto = {
            memberId: parseInt(document.getElementById("memberId").value),
            accountDay: document.getElementById("incomeDate").value,
            source: document.getElementById("incomeSource").value,
            accountMoney: parseInt(amount),
            accountType: '입금',
            bankAccountNum: bankAccountNum,
            bankAccountName: accountName,
            bankName: bankName
        };

        axios.post("/api/income", dto)
            .then(res => {
                alert("입금 완료!");
                closeModal("incomeModal");
            })
            .catch(err => {
                console.error("입금 실패", err);
                alert("입금 실패!");
            });
    });


    function generateRandomBusinessNum() {
        const part1 = String(Math.floor(100 + Math.random() * 900)); // 100~999
        const part2 = String(Math.floor(10 + Math.random() * 90));   // 10~99
        const part3 = String(Math.floor(10000 + Math.random() * 90000)); // 10000~99999
        return `${part1}-${part2}-${part3}`;
    }

    function loadCards() {
        axios.get(`/api/card`)
            .then(response => {
                const cards = response.data; // 배열임
                const select = document.getElementById("expenseCard");
                select.innerHTML = "";

                cards.forEach(card => {
                    const option = document.createElement("option");
                    option.value = card.card_num_masked;            // ✅ snake_case로 수정
                    option.textContent = card.card_name;    // ✅ snake_case로 수정
                    select.appendChild(option);
                });
            })
            .catch(error => {
                console.error("💥 결제용 카드 목록 로딩 실패", error);
            });
    }


    expenseBtn.addEventListener("click", () => {
        loadCards();
        loadCateforyModelList();// 카드 목록 로드
        document.getElementById("expenseModal").style.display = "flex";
    });
    window.saveExpense = function () {
        const cardSelect = document.getElementById("expenseCard");
        const cardNum = cardSelect.value;
        const cardName = cardSelect.options[cardSelect.selectedIndex].textContent; // ✅ 카드 이름 가져오기
        const amount = document.getElementById("expenseAmount").value;
        const category = document.getElementById("categorySelect").value;

        if (!cardNum) {
            alert("카드를 선택하세요.");
            return;
        }

        if (!amount || isNaN(parseInt(amount))) {
            alert("결제 금액을 올바르게 입력하세요.");
            return;
        }

        if (!category) {
            alert("카테고리를 선택하세요.");
            return;
        }

        const dto = {
            cardDay: document.getElementById("expenseDate").value,
            cardNum: cardNum,
            cardName: cardName, // ✅ 추가
            industry: "소매업", // ✅ 임시값 또는 추후 자동 분류
            cardStoreName: document.getElementById("expenseStore").value,
            cardMoney: parseInt(amount),
            businessNum: generateRandomBusinessNum(),
            cardType: "승인",
            categoryName: category
        };

        console.log("📦 전송할 expense dto:", dto);

        axios.post("/api/expense", dto)
            .then(res => {
                alert("결제 완료!");
                closeModal("expenseModal");

                const today = new Date();
                const year = today.getFullYear();
                const month = String(today.getMonth() + 1).padStart(2, '0');
                selectedDate = `${year}-${month}`;
                selectedCategory = null;
                selectedCard = null;

                document.getElementById("selection").value = "expense";
                initializeSelection();
            })
            .catch(err => {
                console.error("결제 실패", err);
                alert("결제에 실패했습니다.");
            });
    };


    function loadCateforyModelList(){
        axios.get("/api/category")
            .then(response => {
                const select = document.getElementById("categorySelect");
                select.innerHTML = '<option value = "">카테고리 선택</option>'
                response.data.forEach(category => {
                    const option = document.createElement("option");
                    option.value = category.category_name;
                    option.textContent = category.category_name;
                    select.appendChild(option);
                });
            })
            .catch(error => {
                console.error("카테고리 목록 로딩 실패", error);
            });
    }

    // 스크롤 높이 자동 조절
    function adjustScrollBoxHeight(boxId) {
        const box = document.getElementById(boxId);
        const buttons = box.querySelectorAll("button");
        if (buttons.length === 0) {
            box.style.height = "0px";
            return;
        }

        const totalHeight = [...buttons].reduce((sum, btn) => {
            const style = window.getComputedStyle(btn);
            const margin = parseInt(style.marginBottom) || 0;
            return sum + btn.offsetHeight + margin;
        }, 0);

        box.style.height = totalHeight + "px";
    }

    // 테이블 변경 감지
    selection.addEventListener("change", function () {
        initializeSelection();
    });

    // 입금/결제 모달
    incomeBtn.addEventListener("click", () => {
        loadAccounts();
        document.getElementById("incomeModal").style.display = "flex";
    });

    expenseBtn.addEventListener("click", () => {
        document.getElementById("expenseModal").style.display = "flex";
    });


    window.closeModal = function (modalId) {
        document.getElementById(modalId).style.display = "none";
    };


    initializeSelection();
});
