window.openModal = function(id) {
    const modal = document.getElementById(id);
    if (!modal) {
        console.error("모달이 존재하지 않습니다:", id);
        return;
    }
    modal.style.display = 'flex';
};

window.closeModal = function(id) {
    const modal = document.getElementById(id);
    if (!modal) return;
    modal.style.display = 'none';

    modal.querySelectorAll('input').forEach(input => {
        if (input.type !== 'hidden' && !input.readOnly) input.value = '';
    });

    modal.querySelectorAll('.error-msg').forEach(err => err.style.display = 'none');

    const timer = modal.querySelector('#timer');
    if (timer) timer.textContent = '';

    const verifyBtn = modal.querySelector('#verifyBtn');
    if (verifyBtn) verifyBtn.disabled = false;
};

window.openPwInputModal = function() {
    if (window.location.pathname === '/account/edit') return; // 현재 페이지가 개인정보 수정이면 무시
    openModal('pwInputModal');
};
window.openEmailModal = function() { openModal('emailModal'); };
window.openResetModal = function() { openModal('resetModal'); };
window.openEmailModalFromPwInput = function() {
    closeModal('pwInputModal');
    openModal('emailModal');
};

window.validatePassword = function() {
    const password = document.getElementById('currentPassword').value;
    const errorDiv = document.getElementById('pwInputError');
    errorDiv.style.display = 'none';

    axios.post('/account/verify-password', { password }, {
        headers: { 'Content-Type': 'application/json' }
    })
        .then(() => window.location.href = "/account/edit")
        .catch(err => {
            errorDiv.style.display = 'block';
            console.error("비밀번호 검증 실패:", err);
        });
};

window.sendVerificationEmail = function() {
    const email = document.getElementById('email').value.trim();
    if (!email) return alert("이메일을 불러오지 못했습니다.");

    axios.post('/api/send-code', { email })
        .then(() => {
            alert('인증 코드가 발송되었습니다.');
            document.getElementById('emailModal').setAttribute('data-email', email);
        })
        .catch(() => alert('이메일 발송 실패'));
};

window.verifyEmailCode = function() {
    const code = document.getElementById('emailCode').value.trim();
    const email = document.getElementById('emailModal').getAttribute('data-email');
    const errorDiv = document.getElementById('emailError');
    errorDiv.style.display = 'none';

    if (!code) {
        errorDiv.textContent = "인증 코드를 입력해주세요.";
        errorDiv.style.display = 'block';
        return;
    }

    axios.post('/api/verify-code', { email, code })
        .then(res => {
            if (res.data.verified) {
                closeModal('emailModal');
                openResetModal();
                alert("인증이 완료되었습니다.");
            } else {
                throw new Error("코드 불일치");
            }
        })
        .catch(() => {
            errorDiv.textContent = "인증에 실패했습니다.";
            errorDiv.style.display = 'block';
        });
};

window.changePassword = function() {
    const newPassword = document.getElementById('newPassword').value;
    const confirmPassword = document.getElementById('confirmNewPassword').value;
    const errorDiv = document.getElementById('resetError');

    if (newPassword !== confirmPassword) {
        errorDiv.textContent = '비밀번호가 일치하지 않습니다.';
        errorDiv.style.display = 'block';
        return;
    }

    axios.post('/account/reset-password', JSON.stringify({ newPassword, confirmPassword }), {
        headers: { 'Content-Type': 'application/json' }
    })
        .then(() => {
            alert('비밀번호가 변경되었습니다.');
            closeModal('resetModal');
        })
        .catch(err => {
            console.error("비밀번호 변경 오류:", err);
            errorDiv.textContent = '변경 중 오류가 발생했습니다.';
            errorDiv.style.display = 'block';
        });
};

// 초기 로딩 시
let countdownInterval;

window.addEventListener("DOMContentLoaded", () => {
    axios.get('/api/user/email')
        .then(response => {
            const email = response.data.email;
            const emailInput = document.getElementById('email');
            if (emailInput) {
                emailInput.value = email;
            }
        })
        .catch(err => {
            console.error('이메일 불러오기 실패:', err);
        });

    const sendBtn = document.getElementById('sendCodeBtn');
    if (sendBtn) {
        sendBtn.addEventListener('click', () => {
            let timeLeft = 300;
            clearInterval(countdownInterval);
            const verifyBtn = document.getElementById('verifyBtn');
            verifyBtn.disabled = false;

            countdownInterval = setInterval(() => {
                const minutes = String(Math.floor(timeLeft / 60)).padStart(2, '0');
                const seconds = String(timeLeft % 60).padStart(2, '0');
                document.getElementById('timer').textContent = `남은 시간: ${minutes}:${seconds}`;
                timeLeft--;

                if (timeLeft < 0) {
                    clearInterval(countdownInterval);
                    document.getElementById('timer').textContent = '인증 시간이 만료되었습니다.';
                    verifyBtn.disabled = true;
                }
            }, 1000);
        });
    }
});
