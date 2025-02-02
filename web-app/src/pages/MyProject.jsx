import React, {useEffect, useRef, useState} from "react";
import {Box, Menu} from "@mui/material";
import ReusableTable from "../components/ReusableTable";
import Scene from "./Scene";
import {isAuthenticated, logOut} from "../services/authenticationService";
import {useLoadUserInfo} from "../services/userService";
import {useNavigate} from "react-router-dom";
import {createProject, getMyProjects, leaveProject, updateProject} from "../services/projectService";
import {handleDate} from "../services/dateTimeService";
import TitleTypography from "../components/TitleTypography";
import ButtonAddTitle from "../components/ButtonAddTitle";
import CustomMenuItem from "../components/CustumMenuItem";
import CreateProjectDialog from "../components/CreateProjectDialog";
import {useSnackbar} from "../context/SnackbarContext";
import UpdateProjectDialog from "../components/UpdateProjectDialog";
import ConfirmDialog from "../components/ConfirmDialog";

const MyProject = () => {

  const [isCreateProjectDialogOpen, setIsCreateProjectDialogOpen] = useState(false);
  const handleOpenCreateProjectDialog = () => {
    setIsCreateProjectDialogOpen(true);
  };
  const handleCloseCreateProjectDialog = () => {
    setIsCreateProjectDialogOpen(false);
  };

  const [isUpdateProjectDialogOpen, setIsUpdateProjectDialogOpen] = useState(false);
  const handleOpenUpdateProjectDialog = () => {
    handleCloseMenuAction();
    setIsUpdateProjectDialogOpen(true);
  };
  const handleCloseUpdateProjectDialog = () => {
    setIsUpdateProjectDialogOpen(false);
  };

  const [confirmDialogOpen, setConfirmDialogOpen] = useState(false);


  const { showSnackbar } = useSnackbar();

  const [selectedRow, setSelectedRow] = useState(null); // State cho selected row (dữ liệu của row)
  const [anchorElRow, setAnchorElRow] = useState(null); // State cho menu hành động

  const [data, setData] = useState([]);
  const [page, setPage] = useState(1);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(true);
  const [hasMore, setHasMore] = useState(false);

  const tableRef = useRef(null);
  const navigate = useNavigate();
  const loadInfo = useLoadUserInfo();

  const columns = [
    { id: "name", label: "Tên", filterable: false },
    { id: "status", label: "Trạng thái", filterable: true, render: (row) => row.status.name },
    { id: "myRole", label: "Vai trò", filterable: true, render: (row) => row.myRole.name },
    { id: "createdAt", label: "Thời gian tạo", filterable: false, render: row => handleDate(row.createdAt) },
  ];

  useEffect(() => {
    loadInfo();
  }, [loadInfo]);

  useEffect(() => {
    const checkAuthentication = async () => {
      if (!await isAuthenticated()) {
        navigate("/login");
      } else {
        loadData(page);
      }
    };

    checkAuthentication();
  }, [navigate, page]);

  const loadData = (page) => {
    setLoading(true);
    getMyProjects(page)
      .then((response) => {
        setTotalPages(response.data.result.totalPages);
        setData((prevData) => [...prevData, ...response.data.result.data]);
        setHasMore(response.data.result.data.length > 0);
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

  const submitCreateProject = (data) => {
    createProject(data).then((response) => {
      const project = response?.data?.result;
      setData((prevData) => [project, ...prevData]);
      showSnackbar("Đã thêm dự án", "success");
    })
      .catch((error) => {
        showSnackbar(error?.response?.data?.message, "error");
      });
  }

  // const handleClickRow = (e, rowId) => {
  //   setSelectedRowId(rowId); // Cập nhật selectedRowId khi click vào dòng
  // };

  const handleDoubleClickRow = (e, row) => {
    navigate("/project/" + row.id);
  }

  const handleClickActionRow = (e, row) => {
    setAnchorElRow(e.currentTarget); // Mở menu hành động
    setSelectedRow(row); // Cập nhật selectedRow khi click vào hành động
  };

  const handleCloseMenuAction = () => {
    setAnchorElRow(null); // Đóng menu
  };

  const submitUpdateProject = (data) => {
    updateProject(selectedRow.id, data)
      .then((response) => {
        showSnackbar("Cập thật thành công", "success");
        const project = response?.data?.result;

        setData((prevData) => prevData.map((row) => {
          if (row.id === project?.id) {
            return project;
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

  const handleLeaveProject = () => {
    leaveProject(selectedRow.id)
      .then((response) => {
        showSnackbar("Rời dự án thành công", "success");

        setData((prevData) => prevData.filter((row) => row.id !== selectedRow.id));
      })
      .catch((error) => {
        showSnackbar(error?.response?.data?.message, "error");
      })
      .finally(() => {
        handleCloseMenuAction();
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
          <TitleTypography>
            Dự án của tôi
          </TitleTypography>
        </Box>

        {/* Right Section: Add Task Button */}
        <ButtonAddTitle onClick={handleOpenCreateProjectDialog}>
          Dự án mới
        </ButtonAddTitle>
      </Box>

      <CreateProjectDialog
        open={isCreateProjectDialogOpen}
        onClose={handleCloseCreateProjectDialog}
        onSubmit={submitCreateProject}
      />
      <UpdateProjectDialog
        open={isUpdateProjectDialogOpen}
        onClose={handleCloseUpdateProjectDialog}
        onSubmit={submitUpdateProject}
        oldData={{name: selectedRow?.name, description: selectedRow?.description}}
      />

      {/*<Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "center", mb: 3 }}>*/}
      {/*  <TitleTypography>*/}
      {/*    Dự án của tôi*/}
      {/*  </TitleTypography>*/}
      {/*</Box>*/}

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

      {/* Menu action */}
      <Menu
        anchorEl={anchorElRow}
        open={Boolean(anchorElRow)}
        onClose={handleCloseMenuAction}
        anchorOrigin={{ vertical: "top", horizontal: "right" }}
        transformOrigin={{ vertical: "top", horizontal: "right" }}
      >
        {selectedRow?.myRole?.name === "MANAGER" && (
          <CustomMenuItem onClick={handleOpenUpdateProjectDialog}>Cập nhật</CustomMenuItem>
        )}
        <CustomMenuItem
          onClick={() => {
            setConfirmDialogOpen(true)
            handleCloseMenuAction();
          }}
        >
          Rời dự án
        </CustomMenuItem>
      </Menu>

      {/* Dialog xác nhận */}
      <ConfirmDialog
        open={confirmDialogOpen}
        onClose={() => setConfirmDialogOpen(false)}
        onConfirm={() => {
          handleLeaveProject();
          setConfirmDialogOpen(false);
        }}
        title="Xác nhận"
        message="Bạn có chắc chắn muốn rời dự án này?"
      />
    </Scene>
  );
};

export default MyProject;
