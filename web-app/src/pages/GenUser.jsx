import Scene from "./Scene";


const GenUser = () => {
    const firstNames = ["Nguyễn", "Trần", "Lê", "Phạm", "Hồ", "Vũ", "Đặng", "Bùi", "Dương", "Hoàng", "Lý", "Đoàn"];
    const middleNames = ["Minh", "Thanh", "Quốc", "Anh", "Thành", "Bảo", "Ngọc", "Hồng", "Quỳnh", "Xuân", "Văn", "Nhật"];
    const lastNames = ["An", "Bảo", "Cường", "Hằng", "Hiền", "Hòa", "Khoa", "Lan", "Long", "Mai", "Minh", "Nhật", "Quang", "Thảo", "Thu"];

    function getRandomFullName() {
        const firstName = firstNames[Math.floor(Math.random() * firstNames.length)];
        const lastName = lastNames[Math.floor(Math.random() * lastNames.length)];

        // Quyết định tên có 3 hay 4 chữ (có thể là họ + tên, hoặc có thêm đệm)
        const useMiddleName = Math.random() > 0.5; // 50% xác suất có tên đệm

        if (useMiddleName) {
            const middleName = middleNames[Math.floor(Math.random() * middleNames.length)];
            return `${firstName} ${middleName} ${lastName}`; // Họ + Tên đệm + Tên
        } else {
            return `${firstName} ${lastName}`; // Họ + Tên
        }
    }

    async function createUser(username, email, fullName) {
        const url = 'http://localhost:8888/api/identity/users/registration';
        const data = {
            username: username,
            password: "matkhau",  // Mật khẩu mặc định
            email: email,
            fullName: fullName
        };

        try {
            const response = await fetch(url, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(data),
            });

            if (response.ok) {
                console.log(`Tạo người dùng ${username} thành công.`);
            } else {
                console.error(`Lỗi khi tạo người dùng ${username}: ${response.statusText}`);
            }
        } catch (error) {
            console.error(`Lỗi khi gửi request cho ${username}: ${error.message}`);
        }
    }

    async function createMultipleUsers() {
        for (let i = 1; i <= 100; i++) {
            const username = `user${String(i).padStart(4, '0')}`; // user0001, user0002, ...
            const email = `user${String(i).padStart(4, '0')}@gmail.com`; // user0001@gmail.com
            const fullName = getRandomFullName(); // Tên ngẫu nhiên

            // Gọi hàm tạo người dùng
            await createUser(username, email, fullName);
        }
    }

    createMultipleUsers();

    return (
        <Scene />
    )
}

export default GenUser;