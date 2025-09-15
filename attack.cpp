#include <iostream>
#include <vector>
#include <utility>
#include <cstdlib>
#include <ctime>
#include <cstdint>

const int PRIME = 257;

// Возведение в степень по модулю
int mod_pow(int base, int exp, int mod) {
    long long res = 1;
    long long b = base;
    while (exp > 0) {
        if (exp & 1) res = (res * b) % mod;
        b = (b * b) % mod;
        exp >>= 1;
    }
    return (int)res;
}

// Восстановление секрета по частям (Лагранж)
int shamir_combine(const std::vector<std::pair<int,int>>& shares) {
    uint64_t secret = 0;
    for (size_t i = 0; i < shares.size(); i++) {
        uint64_t xi = shares[i].first;
        uint64_t yi = shares[i].second;

        uint64_t num = 1, den = 1;
        for (size_t j = 0; j < shares.size(); j++) {
            if (i == j) continue;
            uint64_t xj = shares[j].first;
            num = (num * (-xj + PRIME)) % PRIME;
            den = (den * (xi - xj + PRIME)) % PRIME;
        }
        uint64_t inv = mod_pow((int)den, PRIME-2, PRIME);
        uint64_t term = yi * num % PRIME * inv % PRIME;
        secret = (secret + term) % PRIME;
    }
    return (int)secret;
}

// Деление секрета на части
std::vector<std::pair<int,int>> shamir_split(int secret, int n, int k) {
    std::vector<int> coeffs(k);
    coeffs[0] = secret;
    srand(time(nullptr));
    for (int i = 1; i < k; i++) {
        coeffs[i] = rand() % (PRIME-1) + 1;
    }

    std::vector<std::pair<int,int>> shares;
    for (int x = 1; x <= n; x++) {
        uint64_t y = 0, powX = 1;
        for (int i = 0; i < k; i++) {
            y = (y + coeffs[i] * powX) % PRIME;
            powX = (powX * x) % PRIME;
        }
        shares.push_back({x, (int)y});
    }
    return shares;
}

int main() {
    int secret = rand() % 256 + 1; // случайный ключ XOR
    int n = 5, k = 3;

    std::cout << "Оригинальный секрет: " << secret << std::endl;

    auto shares = shamir_split(secret, n, k);
    std::cout << "Сгенерированные shares:\n";
    for (auto &sh : shares) {
        std::cout << "(" << sh.first << ", " << sh.second << ")\n";
    }

    // Попытка восстановить секрет с 2 частями (не должно работать)
    std::vector<std::pair<int,int>> insufficient = {shares[0], shares[1]};
    int recovered = shamir_combine(insufficient);
    std::cout << "\nПопытка восстановления с 2 частями: " << recovered
              << " (неправильный результат)\n";

    // Восстановление с 3 частями (должно работать)
    std::vector<std::pair<int,int>> enough = {shares[0], shares[1], shares[2]};
    int recovered2 = shamir_combine(enough);
    std::cout << "Восстановление с 3 частями: " << recovered2 << " (правильный результат)\n";

    return 0;
}

