import React, {useEffect, useRef, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import {Box, Menu} from "@mui/material";
import {isAuthenticated, logOut} from "../services/authenticationService";
import Scene from "./Scene";
import {getMyProfile} from "../services/localStorageService";
import IconButton from "@mui/material/IconButton";
import ChevronRightIcon from "@mui/icons-material/ChevronRight";
import TitleTypography from "../components/TitleTypography";
import {handleDate} from "../services/dateTimeService";
import {getProjectById} from "../services/projectService";
import ReusableTable from "../components/ReusableTable";
import PrevTitle from "../components/PrevTitle";
import ButtonAddTitle from "../components/ButtonAddTitle";
import CustomMenuItem from "../components/CustumMenuItem";
import CreateTaskDialog from "../components/CreateTaskDialog";
import {
  assignTask,
  changeTaskStatus,
  createTask,
  deleteTask,
  getTaskInProject,
  unassignTask,
  updateTask
} from "../services/taskService";
import {useSnackbar} from "../context/SnackbarContext";
import ChangeTaskStatusDialog from "../components/ChangeTaskStatusDialog";
import UpdateTaskDialog from "../components/UpdateTaskDialog";
import ConfirmDialog from "../components/ConfirmDialog";
import AssignTaskDialog from "../components/AssignTaskDialog";

const TasksInProject = () => {

  const [confirmDialogOpen, setConfirmDialogOpen] = useState(false);
  const [confirmDeleteDialogOpen, setConfirmDeleteDialogOpen] = useState(false);

  const [isAssignTaskDialogOpen, setIsAssignTaskDialogOpen] = useState(false);
  const handleOpenAssignTaskDialog = () => {
    handleCloseMenuAction();
    setIsAssignTaskDialogOpen(true);
  };
  const handleCloseAssignTaskDialog = () => {
    setIsAssignTaskDialogOpen(false);
  };

  const [isChangeTaskStatusDialogOpen, setIsChangeTaskStatusDialogOpen] = useState(false);
  const handleOpenChangeTaskStatusDialog = () => {
    handleCloseMenuAction();
    setIsChangeTaskStatusDialogOpen(true);
  };
  const handleCloseChangeTaskStatusDialog = () => {
    setIsChangeTaskStatusDialogOpen(false);
  };

  const [isUpdateTaskDialogOpen, setIsUpdateTaskDialogOpen] = useState(false);
  const handleOpenUpdateTaskDialog = () => {
    handleCloseMenuAction();
    setIsUpdateTaskDialogOpen(true);
  };
  const handleCloseUpdateTaskDialog = () => {
    setIsUpdateTaskDialogOpen(false);
  };

  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const handleOpenDialog = () => {
    setIsDialogOpen(true);
  };
  const handleCloseDialog = () => {
    setIsDialogOpen(false);
  };

  const { showSnackbar } = useSnackbar();
  const { projectId } = useParams(); // Lấy projectId từ URL

  const [selectedRow, setSelectedRow] = useState(null); // State cho selected row (dữ liệu của row)
  const [anchorElRow, setAnchorElRow] = useState(null); // State cho menu hành động


  const [data, setData] = useState([]);
  const [page, setPage] = useState(1);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(true);
  const [hasMore, setHasMore] = useState(false);

  const tableRef = useRef(null);
  const navigate = useNavigate();
  const [project, setProject] = useState({});

  const columns = [
    { id: "name", label: "Tên", filterable: false },
    { id: "status", label: "Trạng thái", filterable: true, render: (row) => row.status.name },
    { id: "assigned", label: "Phụ trách bởi", filterable: true,
      render: (row) => row.assignedUserProfile ?
        `${row.assignedUserProfile?.fullName} (${row.assignedUserProfile?.email})` :
        ""
    },
    { id: "project", label: "Thuộc dự án", filterable: true,
      render: (row) => row.projectName },
    { id: "date", label: "Thời gian tạo", filterable: false, render: (row) => handleDate(row.createdAt) },
  ];

  useEffect(() => {
    getProjectById(projectId)
      .then((response) => setProject(response?.data?.result))
      .catch((error) => {
        showSnackbar(error?.response?.data?.message, "error");
      });
  }, []);

  useEffect(() => {
    if (!isAuthenticated()) {
      // navigate("/login");
    } else {
      loadData(page);
    }
  }, [navigate, page]);

  const loadData = (page) => {
    setLoading(true);
    getTaskInProject(projectId, page)
      .then((response) => {
        // setTotalPages(response.data.result.totalPages);
        // setData((prevData) => [...prevData, ...response.data.result.data]);
        // setHasMore(response.data.result.data.length > 0);

        console.log(response);
        setData((prevData) => [...prevData, ...response.data.result]);
        setHasMore(false);
      })
      .catch((error) => {
        if (error.response?.status === 401) {
          logOut();
          navigate("/login");
        } else {
          showSnackbar(error?.response?.data?.message, "error");
        }
      })
      .finally(() => {
        setLoading(false);
      });
  }

  const handleScroll = () => {
    if (!hasMore) return;

    const tableContainer = tableRef.current;
    if (tableContainer) {
      const { scrollTop, scrollHeight, clientHeight } = tableContainer;
      if (scrollTop + clientHeight >= scrollHeight - 100) {
        if (page < totalPages) {
          setPage((prevPage) => prevPage + 1);
        }
        setHasMore(false);
      }
    }
  };

  const submitCreateTask = (data) => {
    createTask(data).then((response) => {
      const task = response?.data?.result;
      setData((prevData) => [task, ...prevData]);
      showSnackbar("Đã thêm công việc", "success");
    })
      .catch((error) => {
        showSnackbar(error?.response?.data?.message, "error");
      });
  }

  const submitChangeTaskStatus = (data) => {
    changeTaskStatus(selectedRow.id, data)
      .then((response) => {
        showSnackbar("Cập thật thành công", "success");
        const task = response?.data?.result;

        setData((prevData) => prevData.map((row) => {
          if (row.id === task?.id) {
            return task;
          }
          return row;
        }));
      })
      .catch((error) => {
        showSnackbar(error?.response?.data?.message, "error");
      })
      .finally(() => {
        handleCloseMenuAction();
      });
  };

  const submitUpdateTask = (data) => {
    updateTask(selectedRow.id, data)
      .then((response) => {
        showSnackbar("Cập thật thành công", "success");
        const task = response?.data?.result;

        setData((prevData) => prevData.map((row) => {
          if (row.id === task?.id) {
            return task;
          }
          return row;
        }));
      })
      .catch((error) => {
        showSnackbar(error?.response?.data?.message, "error");
      });
  };

  const submitAssignTask = (data) => {
    assignTask(selectedRow?.id, data)
      .then((response) => {
        showSnackbar("Cập thật thành công", "success");
        const task = response?.data?.result;

        setData((prevData) => prevData.map((row) => {
          if (row.id === task?.id) {
            return task;
          }
          return row;
        }));
      })
      .catch((error) => {
        showSnackbar(error?.response?.data?.message, "error");
      });
  }

  // const handleClickRow = (e, rowId) => {
  //   setSelectedRowId(rowId); // Cập nhật selectedRowId khi click vào dòng
  // };

  const handleDoubleClickRow = (e, row) => {
    navigate("/task/" + row.id);
  }

  const handleClickActionRow = (e, row) => {
    setAnchorElRow(e.currentTarget); // Mở menu hành động
    setSelectedRow(row); // Cập nhật selectedRow khi click vào hành động
  };

  const handleCloseMenuAction = () => {
    setAnchorElRow(null); // Đóng menu
  };

  const handleDeleteTask = () => {
    deleteTask(selectedRow.id)
      .then((response) => {
        showSnackbar("Đã xóa công việc", "success");

        setData((prevData) => prevData.filter((row) => row.id !== selectedRow.id));
      })
      .catch((error) => {
        showSnackbar(error?.response?.data?.message, "error");
      });
  };

  const handleUnassignTask = () => {
    unassignTask(selectedRow.id)
      .then((response) => {
        showSnackbar("Gở phụ trách thành công", "success");
        const task = response?.data?.result;

        setData((prevData) => prevData.map((row) => {
          if (row.id === task?.id) {
            return task;
          }
          return row;
        }));
      })
      .catch((error) => {
        showSnackbar(error?.response?.data?.message, "error");
      });
  };

  return (
    <Scene>
      <Box
        sx={{
          display: "flex",
          justifyContent: "space-between", // Chia đều không gian giữa các thành phần
          alignItems: "center",
          mb: 3,
        }}
      >
        {/* Left Section: Title */}
        <Box sx={{ display: "flex", alignItems: "center" }}>
          <PrevTitle
            onClick={() => navigate(`/project/${projectId}`)} // Chuyển đến trang project
            sx={{ marginRight: 1 }} // Khoảng cách giữa button và typography
          >
            {project?.name}
          </PrevTitle>
          <IconButton
            sx={{
              padding: 0,
              marginRight: 1,
            }}
          >
            <ChevronRightIcon />
          </IconButton>
          <TitleTypography>
            Công việc
          </TitleTypography>
        </Box>

        {/* Right Section: Add Task Button */}
        {project?.myRole?.name === "MANAGER" && (
          <ButtonAddTitle onClick={handleOpenDialog}>
            Công việc mới
          </ButtonAddTitle>
        )}
      </Box>

      <AssignTaskDialog
        open={isAssignTaskDialogOpen}
        onClose={handleCloseAssignTaskDialog}
        projectId={projectId}
        onSubmit={submitAssignTask}
      />

      <CreateTaskDialog
        open={isDialogOpen}
        onClose={handleCloseDialog}
        projectId={projectId}
        onSubmit={submitCreateTask}
      />

      <ChangeTaskStatusDialog
        open={isChangeTaskStatusDialogOpen}
        onClose={handleCloseChangeTaskStatusDialog}
        onSubmit={submitChangeTaskStatus}
        currentStatus={selectedRow?.status?.name}
      />

      <UpdateTaskDialog
        open={isUpdateTaskDialogOpen}
        onClose={handleCloseUpdateTaskDialog}
        onSubmit={submitUpdateTask}
        oldData={{name: selectedRow?.name, description: selectedRow?.description}}
      />

      <ReusableTable
        columns={columns}
        data={data}
        // selectedRowId={selectedRowId}
        // onRowClick={handleClickRow}
        onDoubleClickRow={handleDoubleClickRow}
        onActionClick={handleClickActionRow}
        loading={loading}
        onScroll={handleScroll}
        ref={tableRef}
        sx={{
          height: "calc(100vh - 260px)",
        }}
      />

      {/* Menu action for task */}
      <Menu
        anchorEl={anchorElRow}
        open={Boolean(anchorElRow)}
        onClose={handleCloseMenuAction}
        anchorOrigin={{vertical: "top", horizontal: "right"}}
        transformOrigin={{vertical: "top", horizontal: "right"}}
      >
        {project?.myRole?.name === "MANAGER" && (
          <CustomMenuItem onClick={handleOpenAssignTaskDialog}>Cập nhật người phụ trách</CustomMenuItem>
        )}
        {project?.myRole?.name === "MANAGER" && (
          <CustomMenuItem
            onClick={() => {
              setConfirmDialogOpen(true)
              handleCloseMenuAction();
            }}
          >
            Gỡ phân công phụ trách
          </CustomMenuItem>
        )}

        {selectedRow?.assignedUserProfile?.userId === getMyProfile()?.userId && (
          <CustomMenuItem onClick={handleOpenChangeTaskStatusDialog}>Cập nhật trạng thái</CustomMenuItem>
        )}
        {selectedRow?.assignedUserProfile?.userId === getMyProfile()?.userId && (
          <CustomMenuItem onClick={handleOpenUpdateTaskDialog}>Cập nhật</CustomMenuItem>
        )}
        {selectedRow?.createdAt &&
          new Date() - new Date(selectedRow.createdAt) < 3600000 && (
            <CustomMenuItem
              onClick={() => {
                setConfirmDeleteDialogOpen(true)
                handleCloseMenuAction();
              }}
            >
              Xóa công việc
            </CustomMenuItem>
          )
        }
      </Menu>

      <ConfirmDialog
        open={confirmDialogOpen}
        onClose={() => setConfirmDialogOpen(false)}
        onConfirm={() => {
          handleUnassignTask();
          setConfirmDialogOpen(false);
        }}
        title="Xác nhận"
        message="Bạn có chắc chắn muốn gỡ phân công phụ trách ở công việc này?"
      />

      <ConfirmDialog
        open={confirmDeleteDialogOpen}
        onClose={() => setConfirmDeleteDialogOpen(false)}
        onConfirm={() => {
          handleDeleteTask();
          setConfirmDeleteDialogOpen(false);
        }}
        title="Xác nhận"
        message="Bạn có chắc chắn muốn xóa công việc này?"
      />
    </Scene>
  );
}

export default TasksInProject;