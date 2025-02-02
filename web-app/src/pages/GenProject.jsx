import Scene from "./Scene";


const GenProject = () => {
    // Các thông tin cần thiết
    const baseUrl = "http://localhost:8888";
    const users = [];  // Mảng chứa thông tin người dùng (user0001 đến user0100)
    const projectNames = [];

// Tạo danh sách projectNames từ Project 0001 đến Project 1000
    for (let i = 1; i <= 1000; i++) {
        projectNames.push(`Project ${String(i).padStart(4, '0')}`);
    }

// Xáo trộn danh sách projectNames
    function shuffleArray(arr) {
        for (let i = arr.length - 1; i > 0; i--) {
            const j = Math.floor(Math.random() * (i + 1));
            [arr[i], arr[j]] = [arr[j], arr[i]]; // Hoán đổi các phần tử
        }
    }

// Hàm đăng nhập để lấy token
    async function loginAndGetToken(username, password) {
        const url = `${baseUrl}/api/identity/auth/token`;
        const data = {
            username: username,
            password: password
        };

        try {
            const response = await fetch(url, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(data),
            });

            const result = await response.json();
            if (result.code === 1000 && result.result.authenticated) {
                return result.result.token; // Trả về token
            } else {
                throw new Error(`Lỗi đăng nhập với ${username}`);
            }
        } catch (error) {
            console.error(error);
        }
    }

// Hàm tạo dự án
    async function createProject(token, projectName, description) {
        const url = `${baseUrl}/api/project/create`;
        const data = {
            name: projectName,
            description: description,
        };

        try {
            const response = await fetch(url, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`,
                },
                body: JSON.stringify(data),
            });

            const result = await response.json();
            if (response.ok && result.code === 1000) {
                console.log(`Tạo dự án "${projectName}" thành công.`);
                return result.result.id; // Trả về projectId
            } else {
                console.error(`Lỗi khi tạo dự án "${projectName}": ${result.message}`);
            }
        } catch (error) {
            console.error(`Lỗi khi tạo dự án "${projectName}": ${error.message}`);
        }
    }

// Hàm lấy userId từ username
    async function getUserId(token, username) {
        const url = `${baseUrl}/api/identity/users/username-to-userId?username=${username}`;

        try {
            const response = await fetch(url, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,
                }
            });

            const result = await response.json();
            if (response.ok && result.code === 1000) {
                return result.result; // Trả về userId
            } else {
                console.error(`Không tìm thấy userId cho ${username}`);
            }
        } catch (error) {
            console.error(`Lỗi khi lấy userId cho ${username}: ${error.message}`);
        }
    }

// Hàm tạo task ngẫu nhiên
    function generateRandomTaskName() {
        // Tạo chuỗi số ngẫu nhiên dài 8 ký tự
        const randomNumber = Math.floor(10000000 + Math.random() * 90000000); // Sinh ra số ngẫu nhiên từ 10000000 đến 99999999
        return `Task ${randomNumber}`;
    }

// Hàm gán trạng thái ngẫu nhiên cho task
    function getRandomTaskStatus() {
        const statuses = ["TODO", "IN_PROCESSING", "COMPLETED"];
        return statuses[Math.floor(Math.random() * statuses.length)];
    }

// Hàm tạo task
    async function createTask(token, projectId, assignedUserId) {
        const taskName = generateRandomTaskName(); // Tên task ngẫu nhiên
        const status = getRandomTaskStatus(); // Trạng thái ngẫu nhiên
        const description = `This is a description for ${taskName}`;

        const url = `${baseUrl}/api/task/create`;
        const data = {
            projectId: projectId,
            name: taskName,
            description: description,
            status: status,
            assignedUserId: assignedUserId
        };

        try {
            const response = await fetch(url, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`,
                },
                body: JSON.stringify(data),
            });

            const result = await response.json();
            if (response.ok && result.code === 1000) {
                console.log(`Tạo task "${taskName}" cho user ${assignedUserId} thành công.`);
            } else {
                console.error(`Lỗi khi tạo task cho user ${assignedUserId}: ${result.message}`);
            }
        } catch (error) {
            console.error(`Lỗi khi tạo task cho user ${assignedUserId}: ${error.message}`);
        }
    }

// Hàm thêm thành viên vào dự án và tạo task
    async function addMemberToProject(token, projectId, userId, role) {
        const url = `${baseUrl}/api/project/member/add`;
        const data = {
            projectId: projectId,
            userId: userId,
            role: role
        };

        try {
            const response = await fetch(url, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`,
                },
                body: JSON.stringify(data),
            });

            const result = await response.json();
            if (response.ok && result.code === 1000) {
                console.log(`Thêm ${role} vào dự án ${projectId} thành công.`);
                if (role === "MEMBER") {
                    // Tạo task cho member
                    await createTask(token, projectId, userId);
                    await createTask(token, projectId, userId);
                    await createTask(token, projectId, userId);
                }
            } else {
                console.error(`Lỗi khi thêm ${role} vào dự án ${projectId}: ${result.message}`);
            }
        } catch (error) {
            console.error(`Lỗi khi thêm thành viên vào dự án ${projectId}: ${error.message}`);
        }
    }

// Hàm phân chia danh sách projectNames cho mỗi người dùng và thêm thành viên
    async function createProjectsForUsers() {
        shuffleArray(projectNames);

        for (let i = 1; i <= 100; i++) {
            const username = `user${String(i).padStart(4, '0')}`;
            const password = "matkhau";

            // Đăng nhập và lấy token
            const token = await loginAndGetToken(username, password);
            if (!token) {
                console.error(`Không thể lấy token cho ${username}`);
                continue;
            }

            // Mỗi người dùng sẽ tạo 10 dự án
            const startIdx = (i - 1) * 10;
            const endIdx = startIdx + 10;
            const userProjectNames = projectNames.slice(startIdx, endIdx);

            // Tạo dự án cho người dùng này
            for (const projectName of userProjectNames) {
                const description = `Here is description for ${projectName}`;
                const projectId = await createProject(token, projectName, description);

                // Lấy userId cho các thành viên (ngẫu nhiên, không phải bản thân)
                const usersToAdd = [];
                for (let j = 1; j <= 7; j++) {
                    let memberIndex = Math.floor(Math.random() * 100) + 1;
                    const memberUsername = `user${String(memberIndex).padStart(4, '0')}`;
                    if (memberUsername !== username && !usersToAdd.includes(memberUsername)) {
                        usersToAdd.push(memberUsername);
                    }
                }

                // Lấy userId của các người dùng và thêm vào dự án
                for (let j = 0; j < 4; j++) {
                    const userId = await getUserId(token, usersToAdd[j]);
                    await addMemberToProject(token, projectId, userId, "MEMBER");
                }
                for (let j = 4; j < 7; j++) {
                    const userId = await getUserId(token, usersToAdd[j]);
                    await addMemberToProject(token, projectId, userId, "VIEWER");
                }
            }
        }
    }

// Chạy hàm tạo dự án cho tất cả người dùng
    createProjectsForUsers();

    return (
        <Scene />
    )
}

export default GenProject;